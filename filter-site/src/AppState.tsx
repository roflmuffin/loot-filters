import { createContext, useContext, useEffect, useState } from 'react';

type Filter = {};

const FilterContext = createContext<Filter>({});

export const useViewportContext = () => useContext(FilterContext);

export function FilterContext({ children }) {
  const [rules, setRules] = useState<unknown[]>([]);

    return <FilterContext.Provider value={{ width, height }}>{children}</ViewportContext.Provider>;
      }
