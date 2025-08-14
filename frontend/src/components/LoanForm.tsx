
import React, { useMemo, useState } from 'react';
import ResultGrid from './ResultGrid';

type FormState = {
  startDate: string;
  endDate: string;
  firstPaymentDate: string;
  loanAmount: string;
  annualInterestRate: string;
};

export const LoanForm: React.FC = () => {
  const [form, setForm] = useState<FormState>({
    startDate: '',
    endDate: '',
    firstPaymentDate: '',
    loanAmount: '',
    annualInterestRate: ''
  });
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<any | null>(null);
  const filled = useMemo(
    () => Object.values(form).every(v => v && v.toString().trim().length > 0),
    [form]
  );

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    try {
      setLoading(true);
      setResult(null);
      const payload = {
        startDate: form.startDate,
        endDate: form.endDate,
        firstPaymentDate: form.firstPaymentDate,
        loanAmount: Number(form.loanAmount),
        annualInterestRate: Number(form.annualInterestRate)
      };
      const res = await fetch('/calculate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      if (!res.ok) throw new Error('Falha ao calcular');
      const json = await res.json();
      setResult(json);
    } catch (err) {
      console.error(err);
      alert('Erro ao calcular. Verifique os dados.');
    } finally {
      setLoading(false);
    }
  }

  function onClear() {
    setForm({
      startDate: '',
      endDate: '',
      firstPaymentDate: '',
      loanAmount: '',
      annualInterestRate: ''
    });
    setResult(null);
  }

  return (
    <form onSubmit={onSubmit} className="mb-4">
      <div className="row g-3 align-items-end">
        <div className="col-12 col-xl-2 col-lg-3">
          <label className="form-label">Data Inicial</label>
          <input
            required
            type="date"
            className="form-control"
            value={form.startDate}
            onChange={e => setForm({ ...form, startDate: e.target.value })}
          />
        </div>
        <div className="col-12 col-xl-2 col-lg-3">
          <label className="form-label">Data Final</label>
          <input
            required
            type="date"
            className="form-control"
            value={form.endDate}
            onChange={e => setForm({ ...form, endDate: e.target.value })}
          />
        </div>
        <div className="col-12 col-xl-2 col-lg-3">
          <label className="form-label">Primeiro Pagamento</label>
          <input
            required
            type="date"
            className="form-control"
            value={form.firstPaymentDate}
            onChange={e => setForm({ ...form, firstPaymentDate: e.target.value })}
          />
        </div>
        <div className="col-12 col-xl-3 col-lg-3">
          <label className="form-label">Valor de Empréstimo</label>
          <input
            required
            type="number"
            className="form-control"
            value={form.loanAmount}
            onChange={e => setForm({ ...form, loanAmount: e.target.value })}
          />
        </div>
        <div className="col-12 col-xl-3 col-lg-3">
          <label className="form-label">Taxa de Juros (%)</label>
          <input
            required
            type="number"
            step="0.01"
            className="form-control"
            value={form.annualInterestRate}
            onChange={e => setForm({ ...form, annualInterestRate: e.target.value })}
          />
        </div>
      </div>

      <div className="mt-3 d-flex gap-2">
        <button type="submit" className="btn btn-primary" disabled={!filled || loading}>
          {loading ? 'Calculando...' : 'Calcular'}
        </button>
        <button type="button" className="btn btn-outline-secondary" onClick={onClear}>
          Limpar
        </button>
      </div>

      {result && <ResultGrid result={result} />}
    </form>
  );
};

export default LoanForm;
