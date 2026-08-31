import type { CargaTrabajo, Expediente, Page, Session, Tarea } from '../types';

// En desarrollo Vite redirige /api al backend, evitando CORS en el navegador.
// Para despliegues separados se puede definir VITE_API_URL con la URL pública del API.
const BASE_URL = import.meta.env.VITE_API_URL ?? '/api/v1';
const TOKEN_KEY = 'deadlineflow_session';

export const session = {
  get: (): Session | null => { try { return JSON.parse(sessionStorage.getItem(TOKEN_KEY) ?? 'null'); } catch { return null; } },
  set: (value: Session) => sessionStorage.setItem(TOKEN_KEY, JSON.stringify(value)),
  clear: () => sessionStorage.removeItem(TOKEN_KEY),
};

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const activeSession = session.get();
  const response = await fetch(`${BASE_URL}${path}`, {
    ...options,
    headers: { 'Content-Type': 'application/json', ...(activeSession ? { Authorization: `Bearer ${activeSession.token}` } : {}), ...options.headers },
  });
  if (!response.ok) {
    const body = await response.json().catch(() => null);
    throw new Error(body?.mensaje ?? body?.message ?? 'No se pudo completar la solicitud.');
  }
  return response.status === 204 ? undefined as T : response.json();
}

export const api = {
  login: (email: string, password: string) => request<Session>('/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) }),
  expedientes: (estado = 'ABIERTO') => request<Page<Expediente>>(`/expedientes?estado=${estado}&page=0&size=30`),
  crearExpediente: (data: { numeroExpediente: string; tipoProceso: string; responsableId: number; honorariosPactados?: number }) => request<Expediente>('/expedientes', { method: 'POST', body: JSON.stringify(data) }),
  tareas: (expedienteId: number) => request<Tarea[]>(`/tareas/expediente/${expedienteId}`),
  crearTarea: (data: { titulo: string; tipo: string; prioridad: string; expedienteId: number; responsableId: number; fechaLimite: string }) => request<Tarea>('/tareas', { method: 'POST', body: JSON.stringify(data) }),
  balance: () => request<CargaTrabajo[]>('/balance-carga'),
};
