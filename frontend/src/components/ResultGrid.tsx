import React from 'react';

type LoanRow = {
  periodDate: string;
  loanAmount: number | null;
  outstandingBalance: number | null;
  installmentLabel: string | null;
  installmentAmount: number | null;
  principalAmortization: number | null;
  balanceAfterPayment: number | null;
  interestForPeriod: number | null;
  accruedInterest: number | null;
  amountPaid: number | null;
};

type Result = {
  installmentCount?: number;
  dayCountBase?: number;
  rows: LoanRow[];
};

function formatCurrency(v: number | null | undefined) {
  if (v === null || v === undefined) return '0,00';
  try {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v as number);
  } catch {
    return `${v}`;
  }
}

function formatDate(d: string | null | undefined) {
  if (!d) return '';
  try {
    const dt = new Date(d);
    return new Intl.DateTimeFormat('pt-BR', { timeZone: 'UTC' }).format(dt);
  } catch {
    return d;
  }
}

export const ResultGrid: React.FC<{ result: Result | null }> = ({ result }) => {
  if (!result || !result.rows || result.rows.length === 0) return null;

  return (
    <div className="table-responsive mt-4">
      <table className="table table-sm table-sm table-bordered align-middle table-hover table-sm table-striped">
        <thead className="table-primary sticky-top">
  <tr className="text-center">
    <th>Data Competência</th>
    <th>Valor de Empréstimo</th>
    <th>Saldo Devedor</th>
    <th>Consolidada</th>
    <th>Total</th>
    <th>Amortização</th>
    <th>Saldo</th>
    <th>Provisão</th>
    <th>Juros Acumulado</th>
    <th>Pago</th>
  </tr>
</thead>
        <tbody>
          {result.rows.map((r, idx) => (
            <tr key={idx}>
              <td>{formatDate(r.periodDate)}</td>
              <td className="text-end">{formatCurrency(r.loanAmount)}</td>
              <td className="text-end">{formatCurrency(r.outstandingBalance)}</td>
              <td className="d-none"></td>
              <td className="text-center">{r.installmentLabel ?? ''}</td>
              <td className="text-end">{formatCurrency(r.installmentAmount)}</td>
              <td className="text-end">{formatCurrency(r.principalAmortization)}</td>
              <td className="text-end">{formatCurrency(r.balanceAfterPayment)}</td>
              <td className="text-end">{formatCurrency(r.interestForPeriod)}</td>
              <td className="text-end">{formatCurrency(r.accruedInterest)}</td>
              <td className="text-end">{formatCurrency(r.amountPaid)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ResultGrid;
