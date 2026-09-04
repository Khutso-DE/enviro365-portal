import { FormEvent, useEffect, useState } from 'react'

type Product = { id: number; type: string; name: string; currentBalance: number }
type Portfolio = { id: number; firstName: string; lastName: string; age: number; products: Product[] }
type Notice = { id: number; productId: number; withdrawalAmount: number; bankingDetails: string; status: string; noticeDate: string }

const investors = [
  { id: 1, name: 'Khutso Nkadimeng', initials: 'KN' },
  { id: 2, name: 'David Lesaomako', initials: 'DL' },
  { id: 3, name: 'Relebohile Mofokeng', initials: 'RM' },
  { id: 4, name: 'Mpho Makola', initials: 'MM' },
]

const money = new Intl.NumberFormat('en-ZA', { style: 'currency', currency: 'ZAR', maximumFractionDigits: 2 })

function App() {
  const [investorId, setInvestorId] = useState(1)
  const [portfolio, setPortfolio] = useState<Portfolio | null>(null)
  const [notices, setNotices] = useState<Notice[]>([])
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null)
  const [amount, setAmount] = useState('')
  const [bankingDetails, setBankingDetails] = useState('')
  const [message, setMessage] = useState('')
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)

  const loadData = async (id: number) => {
    setLoading(true)
    try {
      const [portfolioResponse, noticesResponse] = await Promise.all([
        fetch(`/api/v1/investors/${id}/portfolio`),
        fetch('/api/v1/withdrawals'),
      ])
      if (!portfolioResponse.ok || !noticesResponse.ok) throw new Error('The portfolio service is unavailable.')
      setPortfolio(await portfolioResponse.json())
      setNotices(await noticesResponse.json())
      setMessage('')
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Unable to load portfolio data.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { void loadData(investorId) }, [investorId])

  const totalBalance = portfolio?.products.reduce((sum, product) => sum + product.currentBalance, 0) ?? 0
  const retirementBalance = portfolio?.products.filter((product) => product.type === 'RETIREMENT').reduce((sum, product) => sum + product.currentBalance, 0) ?? 0

  const submitWithdrawal = async (event: FormEvent) => {
    event.preventDefault()
    if (!selectedProduct) return
    setSubmitting(true)
    try {
      const response = await fetch('/api/v1/withdrawals', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productId: selectedProduct.id, withdrawalAmount: Number(amount), bankingDetails }),
      })
      if (!response.ok) {
        const body = await response.json().catch(() => null)
        throw new Error(body?.message ?? 'Withdrawal could not be submitted.')
      }
      setSelectedProduct(null)
      setAmount('')
      setBankingDetails('')
      setMessage('Withdrawal notice submitted successfully.')
      await loadData(investorId)
    } catch (error) {
      setMessage(error instanceof Error ? error.message : 'Withdrawal could not be submitted.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand"><span className="brand-mark">E</span><span>enviro<span>365</span></span></div>
        <div className="sidebar-label">Workspace</div>
        <nav><button className="nav-item active"><span>◈</span> Portfolio</button><button className="nav-item"><span>↗</span> Withdrawals</button></nav>
        <div className="sidebar-footer"><div className="support-dot" /> <div><strong>Advisor support</strong><small>Online today</small></div></div>
      </aside>

      <main className="main-content">
        <header className="topbar"><div><p className="eyebrow">Investor workspace</p><h1>Good morning, {portfolio?.firstName ?? 'investor'}</h1></div><div className="topbar-actions"><button className="icon-button" aria-label="Notifications">♢</button><div className="user-badge">{investors.find((investor) => investor.id === investorId)?.initials}</div></div></header>

        <section className="investor-strip"><div><span className="section-kicker">Viewing portfolio</span><select value={investorId} onChange={(event) => setInvestorId(Number(event.target.value))}>{investors.map((investor) => <option key={investor.id} value={investor.id}>{investor.name}</option>)}</select></div><div className="account-status"><span /> Account active</div></section>

        {message && <div className={message.includes('successfully') ? 'alert success' : 'alert'}>{message}<button onClick={() => setMessage('')} aria-label="Dismiss message">×</button></div>}
        {loading ? <div className="loading">Loading portfolio<span>...</span></div> : portfolio && <>
          <section className="summary-grid"><div className="balance-panel"><div className="panel-heading"><span>Total portfolio value</span><span className="trend">↗ 4.8% this year</span></div><strong>{money.format(totalBalance)}</strong><div className="balance-line"><span>Across {portfolio.products.length} investment products</span><span>{money.format(retirementBalance)} retirement</span></div><div className="progress"><span style={{ width: `${Math.min((retirementBalance / totalBalance) * 100, 100)}%` }} /></div></div><div className="stat-panel"><span className="stat-icon">◎</span><div><small>Investor age</small><strong>{portfolio.age} <em>years</em></strong></div></div><div className="stat-panel"><span className="stat-icon green">↗</span><div><small>Available to withdraw</small><strong>{money.format(totalBalance * 0.9)}</strong></div></div></section>

          <section className="content-grid"><div className="products-section"><div className="section-header"><div><p className="eyebrow">Your investments</p><h2>Products & balances</h2></div><span className="count-pill">{portfolio.products.length} products</span></div><div className="product-list">{portfolio.products.map((product) => <article className="product-card" key={product.id}><div className={`product-icon ${product.type.toLowerCase()}`}>{product.type === 'RETIREMENT' ? '◒' : '◉'}</div><div className="product-info"><div className="product-title"><h3>{product.name}</h3><span>{product.type}</span></div><strong>{money.format(product.currentBalance)}</strong><div className="product-meta"><span>Up to {money.format(product.currentBalance * 0.9)} available</span><button onClick={() => setSelectedProduct(product)}>Request withdrawal <span>→</span></button></div></div></article>)}</div></div><aside className="activity-panel"><div className="section-header"><div><p className="eyebrow">Activity</p><h2>Recent notices</h2></div><button className="text-button" onClick={() => window.open('/api/v1/withdrawals/export', '_blank')}>Export CSV</button></div>{notices.length === 0 ? <p className="empty-state">No withdrawal notices yet.</p> : <div className="notice-list">{notices.slice(-4).reverse().map((notice) => <div className="notice" key={notice.id}><div className="notice-icon">↗</div><div><strong>{money.format(notice.withdrawalAmount)} withdrawal</strong><small>{new Date(notice.noticeDate).toLocaleDateString('en-ZA', { day: 'numeric', month: 'short', year: 'numeric' })}</small></div><span className="status">{notice.status}</span></div>)}</div>}</aside></section>
        </>}
      </main>

      {selectedProduct && <div className="modal-backdrop" onMouseDown={(event) => event.target === event.currentTarget && setSelectedProduct(null)}><div className="modal"><button className="close-button" onClick={() => setSelectedProduct(null)} aria-label="Close withdrawal form">×</button><p className="eyebrow">New withdrawal notice</p><h2>Withdraw from {selectedProduct.name}</h2><p className="modal-copy">You can request up to <strong>{money.format(selectedProduct.currentBalance * 0.9)}</strong> from this product.</p><form onSubmit={submitWithdrawal}><label>Amount<input type="number" min="0.01" max={selectedProduct.currentBalance * 0.9} step="0.01" required value={amount} onChange={(event) => setAmount(event.target.value)} placeholder="0.00" /></label><label>Banking details<input required value={bankingDetails} onChange={(event) => setBankingDetails(event.target.value)} placeholder="Bank and account reference" /></label><button className="primary-button" disabled={submitting}>{submitting ? 'Submitting...' : 'Submit withdrawal notice'} <span>→</span></button></form></div></div>}
    </div>
  )
}

export default App