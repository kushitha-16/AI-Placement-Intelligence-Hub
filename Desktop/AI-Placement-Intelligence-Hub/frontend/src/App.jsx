import { useState } from "react";

function App() {
  const [postText, setPostText] = useState("");
  const [result, setResult] = useState(null);

  const analyzePost = async () => {
    const response = await fetch("http://localhost:8080/api/jobs/analyze", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ postText }),
    });

    const data = await response.json();
    setResult(data);
  };

  return (
    <div style={{ padding: "40px" }}>
      <h1>AI Placement Intelligence Hub</h1>

      <textarea
        rows="10"
        cols="80"
        placeholder="Paste Telegram / WhatsApp placement post here..."
        value={postText}
        onChange={(e) => setPostText(e.target.value)}
      />

      <br /><br />

      <button onClick={analyzePost}>
        Analyze Placement Post
      </button>

      {result && (
        <div style={{ marginTop: "20px" }}>
          <h2>Result</h2>
          <p><b>Company:</b> {result.companyName}</p>
          <p><b>Role:</b> {result.roleName}</p>
          <p><b>Eligibility:</b> {result.eligibility}</p>
          <p><b>Deadline:</b> {result.deadline}</p>
          <p><b>Rounds:</b> {result.selectionRounds}</p>
          <p><b>Skills:</b> {result.requiredSkills?.join(", ")}</p>
        </div>
      )}
    </div>
  );
}

export default App;