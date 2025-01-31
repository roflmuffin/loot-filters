import { useState } from 'react';

import RuleBuilder from './RuleBuilder';

export default function FilterBuilder() {
  const [rules, setRules] = useState<unknown[]>([]);

  return (
    <div>
      <div>
        {rules.map(rule => <RuleBuilder />)}
      </div>
      <button
        onClick={() => setRules([...rules, {}])}
      >add rule</button>
    </div>
  );
};
