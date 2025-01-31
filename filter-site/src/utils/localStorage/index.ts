enum LocalStorageKeys {
  filters = "filters",
}

export const setFiltersToLocalStorage = ({
  filters,
}: {
  filters: unknown[];
}) => {
  localStorage.setItem(LocalStorageKeys.filters, JSON.stringify(filters));
};

export const getFiltersToLocalStorage = () => {
  const filters = localStorage.getItem(LocalStorageKeys.filters);

  if (!filters) return [];
  return JSON.parse(filters);
};
