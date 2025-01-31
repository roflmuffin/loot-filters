interface ItemIconProps {
  readonly image: string;
}

export default function ItemIcon(props: ItemIconProps) {
  return (
    <>
      <div>
        <img src={props.image} />
      </div>
    </>
  );
}
