import { useState } from 'react'
import { LoanForm } from './components/LoanForm'
import { ResultGrid } from './components/ResultGrid'

export default function App(){
  const [data, setData] = useState<any|null>(null)
  return (
    <div className="bg-body text-body min-vh-100">
      <header className="border-bottom bg-white position-sticky top-0" style={{zIndex:10}}>
        <div className="container py-3 d-flex align-items-center">
          <h1 className="h4 m-0">Loan Calculator</h1>
        </div>
      </header>
      <main className="container py-4">
        <section className="card rounded-4 shadow-sm mb-4">
          <div className="card-body">
            <LoanForm onResult={setData} />
          </div>
        </section>
        <section className="card rounded-4 shadow-sm">
          <div className="card-body">
            <ResultGrid data={data} />
          </div>
        </section>
      </main>
    </div>
  )
}
