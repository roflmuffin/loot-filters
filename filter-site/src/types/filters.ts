// type AndRule = {left: Rule, right: Rule}
// type AndRule = {left: Rule, right: Rule}

// type Rule = AndRule | OrRule | ItemNameRule | ItemIdRule | etc.

enum RuleType {
  or = "or",
  and = "and",
  id = "id",
  name = "name",
  value = "value",
  quantity = "quantity",
}

interface RuleModel {
  type: RuleType;
}

interface MatcherType {
  rule: RuleModel;
  displayConfig: string;
}

export interface FilterType {
  matcher: MatcherType[];
}
