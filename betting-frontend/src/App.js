import "./App.css";
import Navbar from "./components/Navbar";
import React from "react";
import Home from "./pages/Home";
import Game from "./pages/Game";
import Participant from "./pages/Participant";
import Participants from "./components/Participants";
import { Route, Routes, Navigate } from "react-router-dom";
import NoPage from "./pages/NoPage";

function App() {
  return (
    <div className="App">
      <Navbar></Navbar>
      <Routes>
        <Route path="/pages/Home" element={<Home></Home>}></Route>
        <Route path="/pages/Game" element={<Game></Game>}></Route>
        <Route
          path="/pages/Participants"
          element={<Participants></Participants>}
        ></Route>
        <Route
          path="/pages/Participant"
          element={<Participant></Participant>}
        ></Route>
        <Route path="/" element={<Navigate replace to="/pages/Home" />}></Route>
        <Route path="*" element={<NoPage />} />
      </Routes>
    </div>
  );
}
export default App;
