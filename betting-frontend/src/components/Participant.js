import React, { useState } from "react";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import { Container } from "@mui/system";
import Paper from "@mui/material/Paper";
import { Button } from "@mui/material";

export default function SubmitParticipant() {
  const [message, setMessage] = useState("");
  const [name, setName] = useState("");
  const [id, setId] = useState("");
  const paperStyle = { padding: "50px 20px", width: 600, margin: "20px auto" };
  const divStyle = { width: 600, margin: "0 auto" };

  function handleClickAdd() {
    const participant = { id, name };
    console.log(participant);
    fetch("http://localhost:8080/participant/add", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(participant),
    })
      .then((response) => response.json())
      .then((response) => {
        console.log(response.message);
        if (response.status === 405) {
          setMessage(response.message);
        } else {
          setMessage("New participant was submitted to the competition!");
        }
      });
  }

  function refreshPage() {
    window.location.reload();
  }

  return (
    <Container>
      <Paper elevation={3} style={paperStyle}>
        <h1>Submit participant</h1>
        <Box
          component="form"
          autoComplete="off"
          noValidate="off"
          sx={{
            "& > :not(style)": { m: 1, width: "25ch" },
          }}
        >
          <TextField
            id="id"
            label="ID"
            variant="outlined"
            value={id}
            onChange={(e) => setId(e.target.value)}
            fullWidth
            required
          />
          <br></br>
          <TextField
            id="name"
            label="Name"
            variant="outlined"
            value={name}
            onChange={(e) => setName(e.target.value)}
            fullWidth
            required
          />
          <div style={divStyle}>{message}</div>
          <br></br>
          <br></br>
          <br></br>

          <Button variant="contained" color="primary" onClick={refreshPage}>
            Refresh
          </Button>
          <Button
            variant="contained"
            color="secondary"
            onClick={handleClickAdd}
          >
            Submit
          </Button>
        </Box>
        <br></br>
      </Paper>
    </Container>
  );
}
