export type Role = 'ASISTENTE' | 'ABOGADO' | 'COORDINADOR' | 'AUDITOR' | 'ADMINISTRADOR';
export type EstadoExpediente = 'ABIERTO' | 'EN_PROCESO' | 'SUSPENDIDO' | 'CERRADO';
export type Prioridad = 'ALTA' | 'MEDIA' | 'BAJA';
export type TipoTarea = 'PROCESAL' | 'ADMINISTRATIVA' | 'DOCUMENTAL' | 'AUDIENCIA';

export interface Session { token: string; email: string; rol: Role }
export interface Expediente { id: number; numeroExpediente: string; tipoProceso: string; estado: EstadoExpediente; responsableNombre: string; honorariosPactados: number | null; fechaApertura: string }
export interface Tarea { id: number; titulo: string; tipo: TipoTarea; prioridad: Prioridad; estado: string; expedienteId: number; responsableNombre: string; fechaLimite: string; esDuplicada: boolean; scoreRiesgo: number }
export interface CargaTrabajo { responsableId: number; responsableNombre: string; cargaActual: number; umbralInterno: number; sobreUmbral: boolean }
export interface Page<T> { content: T[]; totalElements: number; totalPages: number }
