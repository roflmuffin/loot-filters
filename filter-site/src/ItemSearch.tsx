import { useState } from "react";
import Fuse from 'fuse.js';

import ItemIcon from './ItemIcon';

import itemData from "../data/item-db.json";

interface Props {}

const itemData2 = itemData as {
    [key: string]: {
      id: number;
      name: string;
      icon: string;
    };
  };

const itemList = Object.keys(itemData2).map(key => itemData2[key]);

export default function ItemSearch(props: Props) {

  const [search, setSearch] = useState('');

  // @ts-ignore
  const fuse = new Fuse(itemList, {
    keys: ['name'],
  });
  const results = fuse.search(search, { limit: 64 } );

  return (
    <div>
    <input type='text' onChange={e => setSearch(e.target.value)} />
    <div style={{
      display: 'flex',
      flexWrap: 'wrap',
      width:38*5,
      }}>
      {results.map(item => (<ItemIcon key={item.item.id} image={item.item.icon} />))}
    </div>
  </div>
  );
}
