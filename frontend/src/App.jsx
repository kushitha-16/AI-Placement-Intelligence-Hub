import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [activeTab, setActiveTab] = useState("dashboard");

  const [postText, setPostText] = useState("");
  const [result, setResult] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [resumes, setResumes] = useState([]);
  const [loading, setLoading] = useState(false);

  const [resumeFile, setResumeFile] = useState(null);
  const [resumeResult, setResumeResult] = useState(null);
  const [resumeLoading, setResumeLoading] = useState(false);

  const [selectedJobId, setSelectedJobId] = useState("");
  const [selectedResumeId, setSelectedResumeId] = useState("");
  const [matchResult, setMatchResult] = useState(null);

  const [interviewQuestions, setInterviewQuestions] = useState(null);
  const totalJobs = jobs.length;
const appliedCount = jobs.filter((job) => job.applicationStatus === "Applied").length;
const shortlistedCount = jobs.filter((job) => job.applicationStatus === "Shortlisted").length;
const interviewCount = jobs.filter((job) => job.applicationStatus === "Interview").length;
const rejectedCount = jobs.filter((job) => job.applicationStatus === "Rejected").length;
const notAppliedCount = jobs.filter(
  (job) => !job.applicationStatus || job.applicationStatus === "Not Applied"
).length;
  const samplePost = `Greetings from Amazon India!

We are writing to invite eligible students from your institution to register for Amazon ML Summer School 2026.

Registration Link: https://unstop.com/o/KoXsOLD/?ref=amcJFfEZ
Last Date to Register: Sunday, June 14, 2026

Who should apply:
• Students enrolled in B.Tech / M.Tech / PhD
• Pre-final year(2028) or final year of graduation(2027)
• Interest in Machine Learning / Artificial Intelligence

Program details:
• Modules: Supervised Learning, Deep Learning, NLP, RL, Causal Inference, AI Agents, RAG, LLMs
• Selection: 3,000 students selected via 2-round assessment`;

  useEffect(() => {
    fetchJobs();
    fetchResumes();
  }, []);

  const fetchJobs = async () => {
    const response = await fetch("http://localhost:8080/api/jobs");
    const data = await response.json();
    setJobs(data);
  };

  const fetchResumes = async () => {
    const response = await fetch("http://localhost:8080/api/resumes");
    const data = await response.json();
    setResumes(data);
  };

  const analyzePost = async () => {
    if (!postText.trim()) {
      alert("Please paste a placement post first.");
      return;
    }

    try {
      setLoading(true);
      const response = await fetch("http://localhost:8080/api/jobs/analyze", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ postText })
      });

      const data = await response.json();
      setResult(data);
      setPostText("");
      fetchJobs();
    } catch {
      alert("Backend not connected.");
    } finally {
      setLoading(false);
    }
  };

  const uploadResume = async () => {
    if (!resumeFile) {
      alert("Please select a resume PDF first.");
      return;
    }

    try {
      setResumeLoading(true);
      const formData = new FormData();
      formData.append("file", resumeFile);

      const response = await fetch("http://localhost:8080/api/resumes/upload", {
        method: "POST",
        body: formData
      });

      const data = await response.json();
      setResumeResult(data);
      fetchResumes();
    } catch {
      alert("Resume upload failed.");
    } finally {
      setResumeLoading(false);
    }
  };

  const matchResumeWithJob = async () => {
    if (!selectedJobId || !selectedResumeId) {
      alert("Please select both job post and resume.");
      return;
    }

    const response = await fetch("http://localhost:8080/api/match", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        jobPostId: selectedJobId,
        resumeId: selectedResumeId
      })
    });

    const data = await response.json();
    setMatchResult(data);
  };

  const generateInterviewQuestions = async () => {
    if (!selectedJobId || !selectedResumeId) {
      alert("Please select both job post and resume first.");
      return;
    }

    const response = await fetch(
      `http://localhost:8080/api/interview?jobPostId=${selectedJobId}&resumeId=${selectedResumeId}`
    );

    const data = await response.json();
    setInterviewQuestions(data);
  };

  const updateJobStatus = async (id, status) => {
    await fetch(`http://localhost:8080/api/jobs/${id}/status`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ status })
    });

    fetchJobs();
  };

  const deleteJob = async (id) => {
    const confirmDelete = window.confirm("Delete this placement post?");
    if (!confirmDelete) return;

    await fetch(`http://localhost:8080/api/jobs/${id}`, {
      method: "DELETE"
    });

    fetchJobs();
    setResult(null);
  };

  return (
    <div className="page">
      <header className="header">
        <h1>AI Placement Intelligence Hub</h1>
        <p>Analyze posts, upload resumes, and check placement readiness.</p>
      </header>

      <main className="container">
        <div className="tabs">
          <button onClick={() => setActiveTab("dashboard")}>Dashboard</button>
          <button onClick={() => setActiveTab("resume")}>Resume Analyzer</button>
          <button onClick={() => setActiveTab("interview")}>Interview Prep</button>
          <button onClick={() => setActiveTab("applications")}>Applications</button>
        </div>

        {activeTab === "dashboard" && (
          <><section className="card">
  <h2>Placement Analytics Dashboard</h2>

  <div className="analytics-grid">
    <div className="analytics-card">
      <h3>{totalJobs}</h3>
      <p>Total Opportunities</p>
    </div>

    <div className="analytics-card">
      <h3>{notAppliedCount}</h3>
      <p>Not Applied</p>
    </div>

    <div className="analytics-card">
      <h3>{appliedCount}</h3>
      <p>Applied</p>
    </div>

    <div className="analytics-card">
      <h3>{shortlistedCount}</h3>
      <p>Shortlisted</p>
    </div>

    <div className="analytics-card">
      <h3>{interviewCount}</h3>
      <p>Interview</p>
    </div>

    <div className="analytics-card">
      <h3>{rejectedCount}</h3>
      <p>Rejected</p>
    </div>
  </div>
</section>
            <section className="card">
              <div className="section-title">
                <h2>Placement Post Analyzer</h2>
                <button className="sample-btn" onClick={() => setPostText(samplePost)}>
                  Use Sample
                </button>
              </div>

              <textarea
                value={postText}
                onChange={(e) => setPostText(e.target.value)}
                placeholder="Paste placement post here..."
              />

              <button className="primary-btn" onClick={analyzePost} disabled={loading}>
                {loading ? "Analyzing..." : "Analyze Placement Post"}
              </button>
            </section>

            {result && (
              <section className="card result-card">
                <h2>Latest Extracted Result</h2>
                <p><b>Company:</b> {result.companyName}</p>
                <p><b>Role:</b> {result.roleName}</p>
                <p><b>Deadline:</b> {result.deadline}</p>
                <p><b>Eligibility:</b> {result.eligibility}</p>
                <p><b>Skills:</b> {result.requiredSkills?.join(", ")}</p>
              </section>
            )}

            <section className="card">
              <div className="section-title">
                <h2>Saved Placement Posts</h2>
                <button className="sample-btn" onClick={fetchJobs}>Refresh</button>
              </div>

              {jobs.length === 0 ? (
                <p>No saved placement posts yet.</p>
              ) : (
                <div className="job-list">
                  {jobs.map((job) => (
                    <div className="job-card" key={job.id}>
                      <div>
                        <h3>{job.companyName}</h3>
                        <p><b>Role:</b> {job.roleName}</p>
                        <p><b>Deadline:</b> {job.deadline}</p>
                        <p><b>Skills:</b> {job.requiredSkills}</p>
                        <p><b>Status:</b> {job.applicationStatus || "Not Applied"}</p>
                      </div>

                      <button className="delete-btn" onClick={() => deleteJob(job.id)}>
                        Delete
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </section>
          </>
        )}

        {activeTab === "resume" && (
          <>
            <section className="card">
              <h2>Resume Skill Extractor</h2>

              <input
                type="file"
                accept="application/pdf"
                onChange={(e) => setResumeFile(e.target.files[0])}
              />

              <br />
              <br />

              <button className="primary-btn" onClick={uploadResume} disabled={resumeLoading}>
                {resumeLoading ? "Uploading..." : "Upload Resume"}
              </button>

              {resumeResult && (
                <div className="info-box">
                  <h3>Extracted Resume Skills</h3>
                  <p><b>File:</b> {resumeResult.fileName}</p>

                  <div className="tags">
                    {resumeResult.extractedSkills?.map((skill, index) => (
                      <span key={index}>{skill}</span>
                    ))}
                  </div>
                </div>
              )}
            </section>

            <section className="card">
              <h2>Resume vs Job Match Score</h2>

              <label>Select Job Post</label>
              <select value={selectedJobId} onChange={(e) => setSelectedJobId(e.target.value)}>
                <option value="">-- Select Job --</option>
                {jobs.map((job) => (
                  <option key={job.id} value={job.id}>
                    {job.companyName} - {job.roleName}
                  </option>
                ))}
              </select>

              <br /><br />

              <label>Select Resume</label>
              <select value={selectedResumeId} onChange={(e) => setSelectedResumeId(e.target.value)}>
                <option value="">-- Select Resume --</option>
                {resumes.map((resume) => (
                  <option key={resume.id} value={resume.id}>
                    {resume.fileName}
                  </option>
                ))}
              </select>

              <br /><br />

              <button className="primary-btn" onClick={matchResumeWithJob}>
                Check Match Score
              </button>

              {matchResult && (
                <div className="info-box">
                  <h3>Match Result</h3>
                  <p><b>Company:</b> {matchResult.companyName}</p>
                  <p><b>Role:</b> {matchResult.roleName}</p>
                  <p><b>Resume:</b> {matchResult.resumeFileName}</p>
                  <h2>{matchResult.matchScore}% Match</h2>

                  <h3>Matched Skills</h3>
                  <div className="tags">
                    {matchResult.matchedSkills?.map((skill, index) => (
                      <span key={index}>{skill}</span>
                    ))}
                  </div>

                  <h3>Missing Skills</h3>
                  <div className="tags missing">
                    {matchResult.missingSkills?.map((skill, index) => (
                      <span key={index}>{skill}</span>
                    ))}
                  </div>

                  <h3>Personalized Study Plan</h3>
                  <div className="study-plan">
                    {matchResult.studyPlan?.map((item, index) => (
                      <p key={index}>{item}</p>
                    ))}
                  </div>

                  <h3>Resume Improvement Suggestions</h3>
                  <div className="suggestions-box">
                    {matchResult.resumeSuggestions?.map((item, index) => (
                      <p key={index}>✅ {item}</p>
                    ))}
                  </div>
                </div>
              )}
            </section>
          </>
        )}

        {activeTab === "interview" && (
          <section className="card">
            <h2>Interview Prep</h2>

            <label>Select Job Post</label>
            <select value={selectedJobId} onChange={(e) => setSelectedJobId(e.target.value)}>
              <option value="">-- Select Job --</option>
              {jobs.map((job) => (
                <option key={job.id} value={job.id}>
                  {job.companyName} - {job.roleName}
                </option>
              ))}
            </select>

            <br /><br />

            <label>Select Resume</label>
            <select value={selectedResumeId} onChange={(e) => setSelectedResumeId(e.target.value)}>
              <option value="">-- Select Resume --</option>
              {resumes.map((resume) => (
                <option key={resume.id} value={resume.id}>
                  {resume.fileName}
                </option>
              ))}
            </select>

            <br /><br />

            <button className="primary-btn" onClick={generateInterviewQuestions}>
              Generate Interview Questions
            </button>

            {interviewQuestions && (
              <div className="info-box">
                <h3>Interview Questions for {interviewQuestions.companyName}</h3>

                <h4>Technical Questions</h4>
                <ul>
                  {interviewQuestions.technicalQuestions?.map((q, index) => (
                    <li key={index}>{q}</li>
                  ))}
                </ul>

                <h4>HR Questions</h4>
                <ul>
                  {interviewQuestions.hrQuestions?.map((q, index) => (
                    <li key={index}>{q}</li>
                  ))}
                </ul>

                <h4>Project Questions</h4>
                <ul>
                  {interviewQuestions.projectQuestions?.map((q, index) => (
                    <li key={index}>{q}</li>
                  ))}
                </ul>
              </div>
            )}
          </section>
        )}

        {activeTab === "applications" && (
          <section className="card">
            <h2>Application Status Tracker</h2>

            {jobs.length === 0 ? (
              <p>No placement posts available.</p>
            ) : (
              <div className="job-list">
                {jobs.map((job) => (
                  <div className="job-card" key={job.id}>
                    <div>
                      <h3>{job.companyName}</h3>
                      <p><b>Role:</b> {job.roleName}</p>
                      <p><b>Deadline:</b> {job.deadline}</p>
                      <p><b>Status:</b> {job.applicationStatus || "Not Applied"}</p>

                      <select
                        value={job.applicationStatus || "Not Applied"}
                        onChange={(e) => updateJobStatus(job.id, e.target.value)}
                      >
                        <option>Not Applied</option>
                        <option>Applied</option>
                        <option>Shortlisted</option>
                        <option>Interview</option>
                        <option>Rejected</option>
                      </select>
                    </div>

                    <button className="delete-btn" onClick={() => deleteJob(job.id)}>
                      Delete
                    </button>
                  </div>
                ))}
              </div>
            )}
          </section>
        )}
      </main>
    </div>
  );
}

export default App;