function App() {
  const [input, setInput] = useState("");
  const [user, setUser] = useState({
    name: "john",
    email: "john@gmail.com",
    images: ["profile.png", "cover.png"],
  });

  const changeOnlyUser = () => {
    setUser((prev) => ({ ...prev, name: input }));
  };

  return (
    <div>
      <input
        placeholder="Enter a new name.."
        onChange={(e) => setInput(e.target.value)}
      />
      <button onClick={changeOnlyUser}>Change username</button>
      <h2>User:</h2>
      <span>Username: {user?.name}</span>
    </div>
  );
}
