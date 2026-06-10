import { useEffect, useState } from "react";
import jsPDF from "jspdf";
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

  const [mockQuestion, setMockQuestion] = useState("Tell me about yourself.");
  const [mockAnswer, setMockAnswer] = useState("");
  const [mockResult, setMockResult] = useState(null);

  const [readinessResult, setReadinessResult] = useState(null);
  const [selectedReadinessResume, setSelectedReadinessResume] = useState("");

  const [selectedTrackerCompany, setSelectedTrackerCompany] = useState("");
  const [trackerResult, setTrackerResult] = useState(null);

  const [selectedResourceSkill, setSelectedResourceSkill] = useState("");
  const [resourceResult, setResourceResult] = useState(null);

  const [interviewHistory, setInterviewHistory] = useState([]);

  const totalJobs = jobs.length;
  const totalResumes = resumes.length;

  const appliedCount = jobs.filter(
    (job) => job.applicationStatus === "Applied"
  ).length;

  const shortlistedCount = jobs.filter(
    (job) => job.applicationStatus === "Shortlisted"
  ).length;

  const interviewCount = jobs.filter(
    (job) => job.applicationStatus === "Interview"
  ).length;

  const rejectedCount = jobs.filter(
    (job) => job.applicationStatus === "Rejected"
  ).length;

  const notAppliedCount = jobs.filter(
    (job) =>
      !job.applicationStatus ||
      job.applicationStatus === "Not Applied"
  ).length;

  const averageInterviewScore =
    interviewHistory.length === 0
      ? 0
      : Math.round(
          interviewHistory.reduce((sum, item) => sum + item.score, 0) /
            interviewHistory.length
        );

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
    fetchInterviewHistory();
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

  const fetchInterviewHistory = async () => {
    const response = await fetch("http://localhost:8080/api/interview-history");
    const data = await response.json();
    setInterviewHistory(data);
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

  const getDeadlineStatus = (deadlineText) => {
    if (!deadlineText || deadlineText === "Not found") {
      return "No deadline available";
    }

    const deadline = new Date(deadlineText);
    const today = new Date();

    const diffDays = Math.ceil(
      (deadline - today) / (1000 * 60 * 60 * 24)
    );

    if (isNaN(diffDays)) {
      return "Date format not recognized";
    }

    if (diffDays < 0) {
      return "🔴 Deadline Passed";
    }

    if (diffDays <= 3) {
      return `⚠️ Deadline in ${diffDays} day(s)`;
    }

    return `✅ ${diffDays} days remaining`;
  };

  const getCompanyRoadmap = (companyName) => {
    if (!companyName) {
      return ["No company selected."];
    }

    const company = companyName.toLowerCase();

    if (company.includes("cognizant")) {
      return [
        "Revise Java basics and OOPs concepts.",
        "Practice SQL queries: joins, group by, subqueries.",
        "Prepare aptitude topics: percentages, ratios, time and work.",
        "Practice communication round questions.",
        "Revise resume projects for technical interview."
      ];
    }

    if (company.includes("amazon")) {
      return [
        "Revise Machine Learning basics.",
        "Study Deep Learning, NLP, RAG, and LLM concepts.",
        "Practice aptitude and logical reasoning.",
        "Prepare for online assessment rounds.",
        "Explain AI-related projects clearly."
      ];
    }

    if (company.includes("tcs")) {
      return [
        "Practice TCS NQT aptitude and reasoning.",
        "Revise Java/Python programming basics.",
        "Practice coding questions on arrays and strings.",
        "Prepare HR questions.",
        "Revise academic and project fundamentals."
      ];
    }

    if (company.includes("infosys")) {
      return [
        "Practice aptitude and puzzle-based questions.",
        "Revise DBMS, OOPs, and programming basics.",
        "Prepare for technical interview questions.",
        "Practice communication and HR questions.",
        "Review resume projects."
      ];
    }

    return [
      "Revise required skills from the job post.",
      "Practice aptitude and logical reasoning.",
      "Prepare technical interview questions.",
      "Revise resume projects.",
      "Attend one mock interview."
    ];
  };

  const downloadMatchReport = () => {
    if (!matchResult) {
      alert("Please check match score first.");
      return;
    }

    const doc = new jsPDF();
    let y = 15;

    doc.setFontSize(18);
    doc.text("AI Placement Intelligence Hub Report", 15, y);

    y += 12;
    doc.setFontSize(12);
    doc.text(`Company: ${matchResult.companyName}`, 15, y);

    y += 8;
    doc.text(`Role: ${matchResult.roleName}`, 15, y);

    y += 8;
    doc.text(`Resume: ${matchResult.resumeFileName}`, 15, y);

    y += 8;
    doc.text(`Match Score: ${matchResult.matchScore}%`, 15, y);

    y += 12;
    doc.setFontSize(14);
    doc.text("Matched Skills", 15, y);

    y += 8;
    doc.setFontSize(12);
    doc.text(matchResult.matchedSkills?.join(", ") || "None", 15, y);

    y += 12;
    doc.setFontSize(14);
    doc.text("Missing Skills", 15, y);

    y += 8;
    doc.setFontSize(12);
    doc.text(matchResult.missingSkills?.join(", ") || "None", 15, y);

    y += 12;
    doc.setFontSize(14);
    doc.text("Study Plan", 15, y);

    y += 8;
    doc.setFontSize(12);

    matchResult.studyPlan?.forEach((item) => {
      const lines = doc.splitTextToSize(`- ${item}`, 180);
      doc.text(lines, 15, y);
      y += lines.length * 7;

      if (y > 270) {
        doc.addPage();
        y = 15;
      }
    });

    y += 5;
    doc.setFontSize(14);
    doc.text("Resume Suggestions", 15, y);

    y += 8;
    doc.setFontSize(12);

    matchResult.resumeSuggestions?.forEach((item) => {
      const lines = doc.splitTextToSize(`- ${item}`, 180);
      doc.text(lines, 15, y);
      y += lines.length * 7;

      if (y > 270) {
        doc.addPage();
        y = 15;
      }
    });

    y += 5;
    doc.setFontSize(14);
    doc.text("Company Roadmap", 15, y);

    y += 8;
    doc.setFontSize(12);

    getCompanyRoadmap(matchResult.companyName)?.forEach((item) => {
      const lines = doc.splitTextToSize(`- ${item}`, 180);
      doc.text(lines, 15, y);
      y += lines.length * 7;

      if (y > 270) {
        doc.addPage();
        y = 15;
      }
    });

    doc.save("placement-match-report.pdf");
  };

  const getReadinessScore = async () => {
    if (!selectedReadinessResume) {
      alert("Please select a resume.");
      return;
    }

    const response = await fetch(
      `http://localhost:8080/api/readiness/${selectedReadinessResume}`
    );

    const data = await response.json();
    setReadinessResult(data);
  };

  const getPreparationTracker = async () => {
    if (!selectedTrackerCompany) {
      alert("Please select a company.");
      return;
    }

    const response = await fetch(
      `http://localhost:8080/api/preparation/${selectedTrackerCompany}`
    );

    const data = await response.json();
    setTrackerResult(data);
  };

  const getLearningResources = async () => {
    if (!selectedResourceSkill.trim()) {
      alert("Please enter a skill.");
      return;
    }

    const response = await fetch(
      `http://localhost:8080/api/resources/${selectedResourceSkill}`
    );

    const data = await response.json();
    setResourceResult(data);
  };

  const evaluateMockAnswer = async () => {
    if (!mockAnswer.trim()) {
      alert("Please type your answer first.");
      return;
    }

    const response = await fetch("http://localhost:8080/api/mock-interview/evaluate", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        question: mockQuestion,
        answer: mockAnswer
      })
    });

    const data = await response.json();
    setMockResult(data);
    fetchInterviewHistory();
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
          <button onClick={() => setActiveTab("mock")}>Mock Interview</button>
          <button onClick={() => setActiveTab("readiness")}>Readiness</button>
          <button onClick={() => setActiveTab("calendar")}>Calendar</button>
          <button onClick={() => setActiveTab("tracker")}>Preparation Tracker</button>
          <button onClick={() => setActiveTab("resources")}>Resources</button>
          <button onClick={() => setActiveTab("history")}>Interview History</button>
          <button onClick={() => setActiveTab("applications")}>Applications</button>
        </div>

        {activeTab === "dashboard" && (
          <>
            <section className="card">
              <h2>Placement Command Center</h2>

              <div className="analytics-grid">
                <div className="analytics-card">
                  <h3>{totalJobs}</h3>
                  <p>Total Jobs</p>
                </div>

                <div className="analytics-card">
                  <h3>{totalResumes}</h3>
                  <p>Resumes Uploaded</p>
                </div>

                <div className="analytics-card">
                  <h3>{averageInterviewScore}%</h3>
                  <p>Avg Interview Score</p>
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
                <p><b>Alert:</b> {getDeadlineStatus(result.deadline)}</p>
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
                        <p><b>Alert:</b> {getDeadlineStatus(job.deadline)}</p>
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

              <br />
              <br />

              <label>Select Resume</label>
              <select value={selectedResumeId} onChange={(e) => setSelectedResumeId(e.target.value)}>
                <option value="">-- Select Resume --</option>
                {resumes.map((resume) => (
                  <option key={resume.id} value={resume.id}>
                    {resume.fileName}
                  </option>
                ))}
              </select>

              <br />
              <br />

              <button className="primary-btn" onClick={matchResumeWithJob}>
                Check Match Score
              </button>

              <br />
              <br />

              <button className="primary-btn" onClick={downloadMatchReport}>
                Download PDF Report
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

                  <h3>Company Preparation Roadmap</h3>
                  <div className="roadmap-box">
                    {getCompanyRoadmap(matchResult.companyName)?.map((item, index) => (
                      <p key={index}>🎯 {item}</p>
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

            <br />
            <br />

            <label>Select Resume</label>
            <select value={selectedResumeId} onChange={(e) => setSelectedResumeId(e.target.value)}>
              <option value="">-- Select Resume --</option>
              {resumes.map((resume) => (
                <option key={resume.id} value={resume.id}>
                  {resume.fileName}
                </option>
              ))}
            </select>

            <br />
            <br />

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

        {activeTab === "mock" && (
          <section className="card">
            <h2>AI Mock Interview Simulator</h2>

            <label>Select Question</label>
            <select
              value={mockQuestion}
              onChange={(e) => setMockQuestion(e.target.value)}
            >
              <option>Tell me about yourself.</option>
              <option>Why should we hire you?</option>
              <option>Explain your best project.</option>
              <option>What are your strengths and weaknesses?</option>
              <option>Why do you want to join this company?</option>
            </select>

            <br />
            <br />

            <label>Your Answer</label>
            <textarea
              value={mockAnswer}
              onChange={(e) => setMockAnswer(e.target.value)}
              placeholder="Type your interview answer here..."
            />

            <button className="primary-btn" onClick={evaluateMockAnswer}>
              Evaluate Answer
            </button>

            {mockResult && (
              <div className="info-box">
                <h3>Mock Interview Feedback</h3>

                <h2>{mockResult.score || 0}% Score</h2>

                <h3>Strengths</h3>
                <div className="suggestions-box">
                  {mockResult.strengths?.map((item, index) => (
                    <p key={index}>✅ {item}</p>
                  ))}
                </div>

                <h3>Improvements</h3>
                <div className="study-plan">
                  {mockResult.improvements?.map((item, index) => (
                    <p key={index}>🎯 {item}</p>
                  ))}
                </div>
              </div>
            )}
          </section>
        )}

        {activeTab === "readiness" && (
          <section className="card">
            <h2>Placement Readiness Dashboard</h2>

            <label>Select Resume</label>
            <select
              value={selectedReadinessResume}
              onChange={(e) => setSelectedReadinessResume(e.target.value)}
            >
              <option value="">-- Select Resume --</option>
              {resumes.map((resume) => (
                <option key={resume.id} value={resume.id}>
                  {resume.fileName}
                </option>
              ))}
            </select>

            <br />
            <br />

            <button className="primary-btn" onClick={getReadinessScore}>
              Check Readiness
            </button>

            {readinessResult && (
              <div className="info-box">
                <h2>Overall Score: {readinessResult.overallScore}%</h2>
                <p><b>Technical Skills:</b> {readinessResult.technicalScore}%</p>
                <p><b>Projects:</b> {readinessResult.projectScore}%</p>
                <p><b>Communication:</b> {readinessResult.communicationScore}%</p>
                <h3>Status: {readinessResult.status}</h3>
              </div>
            )}
          </section>
        )}

        {activeTab === "calendar" && (
          <section className="card">
            <h2>Placement Calendar</h2>

            {jobs.length === 0 ? (
              <p>No placement posts available.</p>
            ) : (
              <div className="job-list">
                {[...jobs]
                  .sort((a, b) => {
                    const dateA = new Date(a.deadline);
                    const dateB = new Date(b.deadline);
                    return dateA - dateB;
                  })
                  .map((job) => (
                    <div className="job-card" key={job.id}>
                      <h3>{job.companyName}</h3>
                      <p><b>Role:</b> {job.roleName}</p>
                      <p><b>Deadline:</b> {job.deadline}</p>
                      <p><b>Status:</b> {getDeadlineStatus(job.deadline)}</p>
                    </div>
                  ))}
              </div>
            )}
          </section>
        )}

        {activeTab === "tracker" && (
          <section className="card">
            <h2>Company Preparation Tracker</h2>

            <label>Select Company</label>
            <select
              value={selectedTrackerCompany}
              onChange={(e) => setSelectedTrackerCompany(e.target.value)}
            >
              <option value="">-- Select Company --</option>
              <option value="amazon">Amazon</option>
              <option value="cognizant">Cognizant</option>
              <option value="tcs">TCS</option>
              <option value="infosys">Infosys</option>
            </select>

            <br />
            <br />

            <button className="primary-btn" onClick={getPreparationTracker}>
              Show Tracker
            </button>

            {trackerResult && (
              <div className="info-box">
                <h3>{trackerResult.companyName} Preparation Tracker</h3>
                <h2>Progress: {trackerResult.progress}%</h2>

                <div className="study-plan">
                  {trackerResult.topics?.map((topic, index) => (
                    <p key={index}>{topic}</p>
                  ))}
                </div>
              </div>
            )}
          </section>
        )}

        {activeTab === "resources" && (
          <section className="card">
            <h2>Learning Resource Recommender</h2>

            <label>Enter Missing Skill</label>
            <input
              type="text"
              value={selectedResourceSkill}
              onChange={(e) => setSelectedResourceSkill(e.target.value)}
              placeholder="Example: OOPs, Aptitude, SQL, AWS"
            />

            <br />
            <br />

            <button className="primary-btn" onClick={getLearningResources}>
              Get Resources
            </button>

            {resourceResult && (
              <div className="info-box">
                <h3>Resources for {resourceResult.skill}</h3>

                <div className="study-plan">
                  {resourceResult.resources?.map((item, index) => (
                    <p key={index}>📘 {item}</p>
                  ))}
                </div>
              </div>
            )}
          </section>
        )}

        {activeTab === "history" && (
          <section className="card">
            <div className="section-title">
              <h2>Interview History & Performance</h2>
              <button className="sample-btn" onClick={fetchInterviewHistory}>
                Refresh
              </button>
            </div>

            {interviewHistory.length === 0 ? (
              <p>No mock interview attempts yet.</p>
            ) : (
              <div className="job-list">
                <div className="info-box">
                  <h3>Average Score: {averageInterviewScore}%</h3>

                  <h3>
                    Trend:{" "}
                    {interviewHistory.length >= 2 &&
                    interviewHistory[interviewHistory.length - 1].score >
                      interviewHistory[0].score
                      ? "📈 Improving"
                      : "🔄 Keep Practicing"}
                  </h3>
                </div>

                {interviewHistory.map((item, index) => (
                  <div className="job-card" key={item.id}>
                    <div>
                      <h3>Attempt {index + 1}</h3>
                      <p><b>Question:</b> {item.question}</p>
                      <p><b>Score:</b> {item.score}%</p>
                      <p><b>Date:</b> {item.attemptedAt}</p>
                    </div>
                  </div>
                ))}
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