import { EditUserForm } from './edit-user-form'

type User = {
  id: string
  name: string
  email: string
  role: 'admin' | 'user' | 'moderator'
  status: 'active' | 'inactive'
  lastActive: string
}

async function getUser(id: string): Promise<User> {
  // In a real application, you would fetch the user data from an API
  // For this example, we'll use mock data
  return {
    id,
    name: 'John Doe',
    email: 'john@example.com',
    role: 'admin',
    status: 'active',
    lastActive: '2023-05-15T10:30:00Z'
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

