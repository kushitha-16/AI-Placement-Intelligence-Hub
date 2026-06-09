import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [postText, setPostText] = useState("");
  const [result, setResult] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(false);

  const [resumeFile, setResumeFile] = useState(null);
  const [resumeResult, setResumeResult] = useState(null);
  const [resumeLoading, setResumeLoading] = useState(false);

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
  }, []);

  const fetchJobs = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/jobs");
      const data = await response.json();
      setJobs(data);
    } catch (error) {
      console.error("Error fetching jobs:", error);
    }
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
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ postText })
      });

      const data = await response.json();
      setResult(data);
      setPostText("");
      fetchJobs();
    } catch (error) {
      alert("Backend not connected. Start Spring Boot first.");
      console.error(error);
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

      if (!response.ok) {
        throw new Error("Resume upload failed");
      }

      const data = await response.json();
      setResumeResult(data);
    } catch (error) {
      alert("Resume upload failed. Check backend and PDF file.");
      console.error(error);
    } finally {
      setResumeLoading(false);
    }
  };

  const deleteJob = async (id) => {
    const confirmDelete = window.confirm("Delete this placement post?");
    if (!confirmDelete) return;

    try {
      await fetch(`http://localhost:8080/api/jobs/${id}`, {
        method: "DELETE"
      });

      fetchJobs();
      setResult(null);
    } catch (error) {
      alert("Delete failed.");
      console.error(error);
    }
  };

  const useSample = () => {
    setPostText(samplePost);
  };

  return (
    <div className="page">
      <header className="header">
        <h1>AI Placement Intelligence Hub</h1>
        <p>Analyze placement posts, extract job details, upload resumes, and track opportunities.</p>
      </header>

      <main className="container">
        <section className="card">
          <div className="section-title">
            <h2>Placement Post Analyzer</h2>
            <button className="sample-btn" onClick={useSample}>
              Use Sample
            </button>
          </div>

          <textarea
            value={postText}
            onChange={(e) => setPostText(e.target.value)}
            placeholder="Paste Telegram / WhatsApp / Email placement post here..."
          />

          <button className="primary-btn" onClick={analyzePost} disabled={loading}>
            {loading ? "Analyzing..." : "Analyze Placement Post"}
          </button>
        </section>

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

        {result && (
          <section className="card result-card">
            <h2>Latest Extracted Result</h2>

            <div className="result-grid">
              <div>
                <span>Company</span>
                <strong>{result.companyName}</strong>
              </div>

              <div>
                <span>Role / Program</span>
                <strong>{result.roleName}</strong>
              </div>

              <div>
                <span>Deadline</span>
                <strong>{result.deadline}</strong>
              </div>

              <div>
                <span>Apply Link</span>
                <strong>
                  {result.applyLink ? (
                    <a href={result.applyLink} target="_blank" rel="noreferrer">
                      Open Link
                    </a>
                  ) : (
                    "Not found"
                  )}
                </strong>
              </div>
            </div>

            <div className="info-box">
              <h3>Eligibility</h3>
              <p>{result.eligibility}</p>
            </div>

            <div className="info-box">
              <h3>Skills / Topics</h3>
              <div className="tags">
                {result.requiredSkills?.map((skill, index) => (
                  <span key={index}>{skill}</span>
                ))}
              </div>
            </div>

            <div className="info-box">
              <h3>Selection Rounds</h3>
              <p>{result.selectionRounds}</p>
            </div>
          </section>
        )}

        <section className="card">
          <div className="section-title">
            <h2>Saved Placement Posts</h2>
            <button className="sample-btn" onClick={fetchJobs}>
              Refresh
            </button>
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
                  </div>

                  <button className="delete-btn" onClick={() => deleteJob(job.id)}>
                    Delete
                  </button>
                </div>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default App;