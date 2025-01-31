import { createContext, ReactNode, useContext, useState } from "react";
import { FilterType } from "../../types/filters";

interface FilterContextModel {
  filters: FilterType[];
  handleSetFilters?: (filters: FilterType[]) => void;
}

const FilterContext = createContext<FilterContextModel>({ filters: [] });

export const useFilterContext = () => useContext(FilterContext);

export default function FilterProvider({ children }: { children: ReactNode }) {
  const [filters, setFilters] = useState<FilterType[]>([]);

  const handleSetFilters = (filters: FilterType[]) => {
    // manipulate here if needed
    setFilters(filters);
  };

  return (
    <FilterContext.Provider value={{ filters, handleSetFilters }}>
      {children}
    </FilterContext.Provider>
  );
}
