import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Container from "@mui/material/Container";
import Button from "@mui/material/Button";
import AdbIcon from "@mui/icons-material/Adb";
import { Link } from "react-router-dom";
import SearchBar from "./SearchBar";

const pages = ["Home", "Game", "Participant", "Participants"];

const ResponsiveAppBar = () => {
  return (
    <AppBar position="static" color="secondary">
      <Container maxWidth="xl" style={{ display: "block" }}>
        <Toolbar>
          <AdbIcon sx={{ display: { xs: "flex", md: "none" }, mr: 1 }} />
          <Box sx={{ flexGrow: 1, display: { xs: "none", md: "flex" } }}>
            {pages.map((page) => (
              <Button
                key={page}
                sx={{ my: 2, color: "white", display: "block" }}
              >
                <Link
                  style={{ textDecoration: "none", color: "white" }}
                  to={`../pages/${page}`}
                >
                  {page}
                </Link>
              </Button>
            ))}
          </Box>
          <Box>
            <SearchBar></SearchBar>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
};
export default ResponsiveAppBar;
