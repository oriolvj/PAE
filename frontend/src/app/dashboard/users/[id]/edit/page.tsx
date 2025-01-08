import { EditUserForm } from './edit-user-form'

type User = {
  username: string
  nom: string
  edat: number
  tlf: number
  email: string
  rol: Rols
}

type Rols = 'ADMINISTRADOR' | 'GESTOR_PROJECTE' | 'TREBALLADOR'

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
      rol: ['ADMINISTRADOR', 'GESTOR_PROJECTE', 'TREBALLADOR'].includes(data.rol) ? data.rol as Rols : 'TREBALLADOR'
    }

    return user
  } catch (error) {
    console.error(error)
    throw error
  }
}

type PageProps = {
  params: Promise<{
    id: string
  }>
}

export default async function Page({ params }: PageProps) {
  const { id } = await params
  const user = await getUser(id)

  return <EditUserForm initialUser={user} />
}
