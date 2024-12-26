import { EditUserForm } from './edit-user-form'

type User = {
  username: string
  nom: string
  edat: number
  tlf: number
  email: string
  rol: Rols
  preferencia: string
  actiu: boolean
  contractat: boolean
  jornada: Jornada
}

type Rols = 'ADMINISTRADOR' | 'GESTOR_PROJECTE' | 'TREBALLADOR'
type Jornada = 'TOTAL' | 'PARCIAL' | 'TRENTA_HORES' | 'ALTRES'

async function getUser(id: string): Promise<User> {
  try {
    const response = await fetch(`http://10.4.41.40:8080/usuaris/${id}`)
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    const data = await response.json()

    console.log(data)
    
    // Type checking to ensure the returned data matches our User type
    const user: User = {
      username: data.username || '',
      nom: data.nom || '',
      edat: typeof data.edat === 'number' ? data.edat : 0,
      tlf: typeof data.tlf === 'number' ? data.tlf : 0,
      email: data.email || '',
      rol: ['ADMINISTRADOR', 'GESTOR_PROJECTE', 'TREBALLADOR'].includes(data.rol) ? data.rol as Rols : 'TREBALLADOR',
      preferencia: data.preferencia || '',
      actiu: Boolean(data.actiu),
      contractat: Boolean(data.contractat),
      jornada: ['TOTAL', 'PARCIAL', 'TRENTA_HORES', 'ALTRES'].includes(data.jornada) ? data.jornada as Jornada : 'ALTRES'
    }
    
    console.log(user)

    return user
  } catch (error) {
    console.error('Error fetching user:', error)
    throw error
  }
}

export default async function EditUserPage({ params }: { params: { id: string } }) {
  const user = await getUser(params.id)

  return (
    <div className="container mx-auto py-10">
      <h1 className="text-3xl font-bold mb-6">Edit User</h1>
      <EditUserForm initialUser={user} />
    </div>
  )
}

