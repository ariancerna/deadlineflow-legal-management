import { useState } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { session } from './lib/api';
import type { Session } from './types';
import { AppLayout } from './components/AppLayout';
import { Login } from './pages/Login';
import { Dashboard } from './pages/Dashboard';
import { Expedientes } from './pages/Expedientes';
import { NuevoExpediente } from './pages/NuevoExpediente';
import { NuevaTarea } from './pages/NuevaTarea';
import { BalanceCarga } from './pages/BalanceCarga';

function Protected({ user, children }: { user: Session | null; children: React.ReactNode }) {
  return user ? <AppLayout user={user}>{children}</AppLayout> : <Navigate to="/login" replace />;
}

export default function App() {
  const [user, setUser] = useState<Session | null>(session.get());
  const logout = () => { session.clear(); setUser(null); };
  return <Routes>
    <Route path="/login" element={user ? <Navigate to="/dashboard" replace /> : <Login onLogin={setUser} />} />
    <Route path="/dashboard" element={<Protected user={user}><Dashboard /></Protected>} />
    <Route path="/expedientes" element={<Protected user={user}><Expedientes /></Protected>} />
    <Route path="/expedientes/nuevo" element={<Protected user={user}><NuevoExpediente /></Protected>} />
    <Route path="/expedientes/:id/tareas/nueva" element={<Protected user={user}><NuevaTarea /></Protected>} />
    <Route path="/balance-carga" element={<Protected user={user}>{user && ['COORDINADOR', 'ADMINISTRADOR'].includes(user.rol) ? <BalanceCarga /> : <Navigate to="/dashboard" replace />}</Protected>} />
    <Route path="*" element={<Navigate to={user ? '/dashboard' : '/login'} replace />} />
  </Routes>;
}
