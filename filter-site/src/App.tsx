import "./App.css";

import ItemSearch from './ItemSearch';
import FilterBuilder from './FilterBuilder';

function App() {
  return (
      <div style={{
        display: 'flex',
        }} >
        <ItemSearch />
        <FilterBuilder />
      </div>
  );
}

export default App;
