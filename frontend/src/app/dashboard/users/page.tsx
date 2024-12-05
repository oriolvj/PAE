'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { PlusCircle } from 'lucide-react'
import { Skeleton } from "@/components/ui/skeleton"

type Rols = 'ADMINISTRADOR' | 'GESTOR_PROJECTE' | 'TREBALLADOR'
type Jornada = 'Total' | 'Parcial' | 'Trenta_hores'

type User = {
  username: string
  nom: string
  edat: number
  tlf: number
  email: string
  pwd: string
  rol: Rols
  preferencia: string
  actiu: boolean
  contractat: boolean
  jornada: Jornada
}

export default function UsersPage() {
  const router = useRouter()
  const [searchTerm, setSearchTerm] = useState('')
  const [users, setUsers] = useState<User[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await fetch('http://10.4.41.41:8080/usuaris')
        if (!response.ok) {
          throw new Error('Hi ha hagut un error en recuperar els usuaris.')
        }
        const data = await response.json()
        setUsers(data)
        setIsLoading(false)
      } catch (err) {
        setError('Hi ha hagut un error en recuperar els usuaris. Torna-ho a provar.')
        setIsLoading(false)
      }
    }

    fetchUsers()
  }, [])

  const filteredUsers = users.filter(user =>
    user.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    user.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
    user.username.toLowerCase().includes(searchTerm.toLowerCase())
  )

  if (error) {
    return <div className="text-center text-red-500 mt-8">{error}</div>
  }

  return (
    <div className="container mx-auto py-10">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">User Management</h1>
        <Button onClick={() => router.push('/dashboard/users/add')}>
          <PlusCircle className="mr-2 h-4 w-4" /> Add New User
        </Button>
      </div>
      <div className="mb-4">
        <Input
          placeholder="Search users..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="max-w-sm"
        />
      </div>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead className="w-[50px]">Avatar</TableHead>
            <TableHead>Nom</TableHead>
            <TableHead>Nom d'usuari</TableHead>
            <TableHead>Correu</TableHead>
            <TableHead>Edat</TableHead>
            <TableHead>Rol</TableHead>
            <TableHead>Estat</TableHead>
            <TableHead>Jornada</TableHead>
            <TableHead>Contractat</TableHead>
            <TableHead className="text-right">Accions</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {isLoading ? (
            Array(5).fill(0).map((_, index) => (
              <TableRow key={index}>
                <TableCell><Skeleton className="h-10 w-10 rounded-full" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[200px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[150px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[200px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[50px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
                <TableCell><Skeleton className="h-4 w-[100px]" /></TableCell>
              </TableRow>
            ))
          ) : (
            filteredUsers.map((user, index) => (
              <TableRow key={index}>
                <TableCell>
                  <Avatar>
                    <AvatarImage src={`https://api.dicebear.com/6.x/initials/svg?seed=${user.nom}`} />
                    <AvatarFallback>{user.nom.split(' ').map(n => n[0]).join('')}</AvatarFallback>
                  </Avatar>
                </TableCell>
                <TableCell className="font-medium">{user.nom}</TableCell>
                <TableCell>{user.username}</TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>{user.edat}</TableCell>
                <TableCell>
                  <Badge variant={user.rol === 'ADMINISTRADOR' ? 'destructive' : user.rol === 'GESTOR_PROJECTE' ? 'outline' : 'default'}>
                    {user.rol}
                  </Badge>
                </TableCell>
                <TableCell>
                  <Badge variant={user.actiu ? 'default' : 'secondary'}>
                    {user.actiu ? 'Active' : 'Inactive'}
                  </Badge>
                </TableCell>
                <TableCell>
                  <Badge variant={user.jornada === 'Total' ? 'default' : 'outline'}>
                    {user.jornada}
                  </Badge>
                </TableCell>
                <TableCell>
                  <Badge variant={user.contractat ? 'default' : 'secondary'}>
                    {user.contractat ? 'Contracted' : 'Not Contracted'}
                  </Badge>
                </TableCell>
                <TableCell className="text-right">
                  <Button 
                    variant="ghost" 
                    size="sm" 
                    onClick={() => router.push(`/dashboard/users/${user.username}/edit`)}
                  >
                    Edit
                  </Button>
                  <Button variant="ghost" size="sm" className="text-red-600">Delete</Button>
                </TableCell>
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>
    </div>
  )
}

