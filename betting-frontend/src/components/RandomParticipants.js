import React, { useState } from "react";
import { Button } from "@mui/material";
import { Container } from "@mui/system";
import Paper from "@mui/material/Paper";

export default function RandomParticipants() {
  const [randomParticipants, setRandomParticipants] = useState([]);
  const paperStyle = { padding: "50px 20px", width: 600, margin: "20px auto" };

  function getRandom() {
    fetch("http://localhost:8080/participant/getRandom", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((response) => {
        console.log(response);
        setRandomParticipants(response);
      });
  }

  return (
    <Container>
      <Paper elevation={3} style={paperStyle}>
        <Button variant="contained" color="secondary" onClick={getRandom}>
          Generate Game
        </Button>
        <div>
          <h1>Today's match</h1>
          {
            <ul>
              {Object.values(randomParticipants).map((participant, i) => (
                <Paper
                  elevation={6}
                  style={{ margin: "10px", padding: "15px", textAlign: "left" }}
                  key={i}
                >
                  {participant.name}
                </Paper>
              ))}
            </ul>
          }
        </div>
      </Paper>
    </Container>
  );
}
