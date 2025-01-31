import RuleBuilder from "./RuleBuilder";
import { useFilterContext } from "./utils/providers/FilterProvider";

export default function FilterBuilder() {
  const { filters, handleSetFilters } = useFilterContext();

  return (
    <div>
      <div>
        {/* {rules.map((rule) => (
          <RuleBuilder />
        ))} */}
      </div>
      {/* <button onClick={() => setRules([...rules, {}])}>add rule</button> */}
    </div>
  );
}
