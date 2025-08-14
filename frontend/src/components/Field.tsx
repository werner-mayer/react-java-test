import { InputHTMLAttributes } from 'react'

type Props = InputHTMLAttributes<HTMLInputElement> & { label: string }

export function Field({ label, ...rest }: Props){
  return (
    <div className="col">
      <label className="form-label small">{label}</label>
      <input className="form-control" {...rest} />
    </div>
  )
}
