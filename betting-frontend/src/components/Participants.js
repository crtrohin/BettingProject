import React, { useEffect, useState } from "react";
import Pagination from "@mui/material/Pagination";
import { Container } from "@mui/system";
import Paper from "@mui/material/Paper";
import DeleteIcon from "@mui/icons-material/Delete";
import { IconButton } from "@mui/material";
import { Button } from "@mui/material";
import { Dialog } from "@mui/material";
import { DialogContent } from "@mui/material";
import { TextField } from "@mui/material";
import { DialogActions } from "@mui/material";
import { Box } from "@mui/system";

export default function Participants() {
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;
  const totalItems = 30;

  const count = Math.ceil(totalItems / itemsPerPage);

  const paperStyle = { padding: "50px 20px", width: 600, margin: "20px auto" };

  const maxPage = Math.ceil(totalItems / itemsPerPage);
  const [data, setData] = useState([]);

  const handleChange = (e, p) => {
    const pageNumber = Math.max(1, p);
    setCurrentPage(Math.min(pageNumber, maxPage));
  };

  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  useEffect(() => {
    fetch(
      `http://localhost:8080/participant/getAll?page=${currentPage -
        1}&perPage=${itemsPerPage}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    )
      .then((response) => response.json())
      .then((response) => {
        setData(response);
      });
  });

  function deleteParticipant(id) {
    fetch("http://localhost:8080/participant/deleteParticipant/" + id, {
      method: "DELETE",
    })
      .then((response) => response.json())
      .then((response) => console.log(response));
  }

  const [id, setId] = useState("");
  const [name, setName] = useState("");

  function handleClickUpdate() {
    const participant = { id, name };
    console.log(participant);
    fetch(`http://localhost:8080/participant/update/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(participant),
    })
      .then((response) => response.json())
      .then((response) => {
        console.log(response.message);
      });

    handleClose();
    setId("");
    setName("");
  }

  return (
    <Container>
      <Paper elevation={1} style={paperStyle}>
        <h1>All Participants</h1>
        {data.map((participant) => (
          <Paper
            elevation={3}
            style={{
              margin: "10px",
              padding: "15px",
              textAlign: "left",
              alignItems: "right",
              display: "block",
            }}
            key={participant.id}
          >
            {participant.name}
            <Button
              variant="contained"
              color="inherit"
              sx={{ float: "right", marginTop: "-10px" }}
              onClick={handleClickOpen}
            >
              Update
            </Button>
            <IconButton
              onClick={() => deleteParticipant(participant.id)}
              sx={{ float: "right", marginTop: "-10px" }}
            >
              <DeleteIcon />
            </IconButton>
            <Dialog
              open={open}
              onClose={handleClose}
              BackdropProps={{ invisible: true }}
            >
              <DialogContent>
                <h1>Update participant</h1>
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
                  <TextField
                    id="name"
                    label="New Name"
                    variant="outlined"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    fullWidth
                    required
                  />

                  <br></br>
                  <br></br>
                  <br></br>
                </Box>
              </DialogContent>
              <DialogActions>
                <Button
                  variant="contained"
                  color="secondary"
                  onClick={handleClickUpdate}
                >
                  Submit
                </Button>
              </DialogActions>
            </Dialog>
          </Paper>
        ))}
      </Paper>
      <Pagination
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
        color="secondary"
        count={count}
        size="large"
        page={currentPage}
        variant="outlined"
        shape="rounded"
        onChange={handleChange}
      />
    </Container>
  );
}
