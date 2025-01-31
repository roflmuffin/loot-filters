import "./App.css";

import ItemSearch from "./ItemSearch";
import FilterBuilder from "./FilterBuilder";
import FilterProvider from "./utils/providers/FilterProvider";

function App() {
  return (
    <FilterProvider>
      <div
        style={{
          display: "flex",
        }}
      >
        <ItemSearch />
        <FilterBuilder />
      </div>
    </FilterProvider>
  );
}

export default App;
