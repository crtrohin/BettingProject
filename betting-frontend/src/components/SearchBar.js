import React, { useState } from "react";
import Paper from "@mui/material/Paper";
import InputBase from "@mui/material/InputBase";
import IconButton from "@mui/material/IconButton";
import SearchIcon from "@mui/icons-material/Search";
import Container from "@mui/material/Container";

function SearchBar() {
  const [row, setRow] = useState("");
  const [searched, setSearched] = useState("");

  const requestSearch = () => {
    fetch("http://localhost:8080/participant/getByName/" + searched, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((response) => setRow(response));
  };

  return (
    <Container>
      <Paper
        component="form"
        sx={{ p: "2px 4px", display: "flex", alignItems: "center", width: 250 }}
      >
        <InputBase
          sx={{ ml: 1, flex: 1 }}
          placeholder="Search participant"
          value={searched}
          onChange={(event) => setSearched(event.target.value)}
        />
        <IconButton
          sx={{ p: "10px" }}
          aria-label="search"
          onClick={() => requestSearch()}
        >
          <SearchIcon />
        </IconButton>
      </Paper>

      <Paper>
        <div> {row.name} </div>
        <div> {row.id} </div>
      </Paper>
    </Container>
  );
}

export default SearchBar;
