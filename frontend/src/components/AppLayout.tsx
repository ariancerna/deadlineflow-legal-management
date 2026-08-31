import { BriefcaseBusiness, LayoutDashboard, LogOut, Scale, UsersRound } from 'lucide-react';
import { NavLink, useNavigate } from 'react-router-dom';
import { session } from '../lib/api';
import type { Session } from '../types';

const links = [{ to: '/dashboard', label: 'Resumen', icon: LayoutDashboard }, { to: '/expedientes', label: 'Expedientes', icon: BriefcaseBusiness }];
export function AppLayout({ user, children }: { user: Session; children: React.ReactNode }) {
  const navigate = useNavigate();
  const logout = () => { session.clear(); navigate('/login'); window.location.reload(); };
  return <div className="min-h-screen bg-slate-50 text-slate-900">
    <aside className="fixed inset-y-0 hidden w-72 flex-col bg-slate-950 p-6 text-slate-300 lg:flex">
      <div className="mb-12 flex items-center gap-3 text-white"><span className="grid size-10 place-items-center rounded-xl bg-cyan-400 text-slate-950"><Scale size={22} /></span><div><p className="font-bold tracking-tight">Deadlineflow</p><p className="text-xs text-slate-500">Gestión legal</p></div></div>
      <nav className="space-y-2">{links.map(({ to, label, icon: Icon }) => <NavLink key={to} to={to} className={({ isActive }) => `nav-link ${isActive ? 'nav-link-active' : ''}`}><Icon size={19} />{label}</NavLink>)}
      {['COORDINADOR', 'ADMINISTRADOR'].includes(user.rol) && <NavLink to="/balance-carga" className={({ isActive }) => `nav-link ${isActive ? 'nav-link-active' : ''}`}><UsersRound size={19} />Carga de trabajo</NavLink>}</nav>
      <div className="mt-auto rounded-2xl border border-slate-800 bg-slate-900/70 p-4"><p className="truncate text-sm font-medium text-white">{user.email}</p><p className="mt-1 text-xs font-semibold tracking-wide text-cyan-300">{user.rol}</p><button onClick={logout} className="mt-4 flex items-center gap-2 text-sm text-slate-400 transition hover:text-white"><LogOut size={16} />Cerrar sesión</button></div>
    </aside>
    <main className="min-h-screen lg:ml-72"><header className="flex h-20 items-center justify-between border-b border-slate-200 bg-white px-5 lg:px-10"><div className="flex items-center gap-2 lg:hidden"><Scale className="text-cyan-600" /><span className="font-bold">Deadlineflow</span></div><p className="ml-auto text-sm text-slate-500">Panel de gestión</p></header><div className="mx-auto max-w-7xl p-5 lg:p-10">{children}</div></main>
  </div>;
}
