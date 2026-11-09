export default function CadastroSelect({ label, name, options, describe, defaultValue = '' }) {
  return (
    <label>
      {label}
      <select name={name} required defaultValue={defaultValue}>
        <option value="" disabled>Selecione</option>
        {options.map(item => <option key={item.id} value={item.id}>{describe ? describe(item) : item.nome}</option>)}
      </select>
    </label>
  );
}
