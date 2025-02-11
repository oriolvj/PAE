'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { PlusCircle, Edit, Trash2, Mail, Hash } from 'lucide-react'
import { Skeleton } from "@/components/ui/skeleton"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { revalidatePath } from 'next/cache'
import { toast } from '@/hooks/use-toast'

type Rols = 'ADMINISTRADOR' | 'GESTOR_PROJECTE' | 'TREBALLADOR'

type User = {
  username: string
  nom: string
  edat: number
  tlf: number
  email: string
  rol: Rols
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
        const response = await fetch('http://10.4.41.33:8080/usuaris')
        if (!response.ok) {
          throw new Error('Hi ha hagut un error en recuperar els usuaris.')
        }
        const data = await response.json()
        console.log(data)
        setUsers(data)
        setIsLoading(false)
      } catch (err) {
        console.error(err)
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

  async function deleteUser(username: string) {
    try {
      const response = await fetch(`http://10.4.41.33:8080/usuaris/${username}`, {
        method: 'DELETE',
      })
  
      if (!response.ok) {
        throw new Error('Failed to delete user')
      }
  
      revalidatePath('/dashboard/users')
      return { success: true }
    } catch (error) {
      console.error('Error deleting user:', error)
      return { success: false, error: 'Failed to delete user' }
    }
  }

  const handleDeleteUser = async (username: string) => {
    try {
      const result = await deleteUser(username)
      if (result.success) {
        setUsers(users.filter(user => user.username !== username))
        toast({
          title: "Èxit",
          description: "Usuari eliminat correctament",
        })
      } else {
        throw new Error(result.error)
      }
    } catch (error) {
      console.error('Error esborrant l\'usuari:', error)
      toast({
        title: "Error",
        description: "No s'ha pogut eliminar l'usuari. Torna-ho a provar.",
        variant: "destructive",
      })
    }
  }

  if (error) {
    return (
      <Card className="max-w-md mx-auto mt-8">
        <CardContent className="pt-6">
          <div className="text-center text-red-500">{error}</div>
        </CardContent>
      </Card>
    )
  }

  return (
    <div className="container mx-auto py-10">
      <Card className="mb-8">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-2xl font-bold">Gestió d&apos;Usuaris</CardTitle>
          <Button onClick={() => router.push('/dashboard/users/add')}>
            <PlusCircle className="mr-2 h-4 w-4" /> Afegir Nou Usuari
          </Button>
        </CardHeader>
        <CardContent>
          <Input
            placeholder="Buscar usuaris..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="max-w-sm mb-4"
          />
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead className="w-[50px]">Avatar</TableHead>
                <TableHead>Nom</TableHead>
                <TableHead>Nom d&apos;usuari</TableHead>
                <TableHead>Correu</TableHead>
                <TableHead>Telefon</TableHead>
                <TableHead>Edat</TableHead>
                <TableHead>Rol</TableHead>
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
                    <TableCell><Skeleton className="h-4 w-[200px]" /></TableCell>
                    <TableCell><Skeleton className="h-4 w-[50px]" /></TableCell>
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
                    <TableCell className="font-medium">
                      <div className="flex items-center">
                        {user.nom}
                      </div>
                    </TableCell>
                    <TableCell>{user.username}</TableCell>
                    <TableCell>
                      <div className="flex items-center">
                        <Mail className="mr-2 h-4 w-4 text-muted-foreground" />
                        {user.email}
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center">
                        <Hash className="mr-2 h-4 w-4 text-muted-foreground" />
                        {user.tlf}
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center">
                        {user.edat}
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge variant={user.rol === 'ADMINISTRADOR' ? 'destructive' : user.rol === 'GESTOR_PROJECTE' ? 'outline' : 'default'}>
                        {user.rol}
                      </Badge>
                    </TableCell>
                    <TableCell className="text-right">
                      <Button 
                        variant="ghost" 
                        size="sm" 
                        onClick={() => router.push(`/dashboard/users/${user.username}/edit`)}
                      >
                        <Edit className="mr-2 h-4 w-4" />
                        Editar
                      </Button>
                      <Button 
                        variant="ghost" 
                        size="sm" 
                        className="text-red-600"
                        onClick={() => handleDeleteUser(user.username)}
                      >
                        <Trash2 className="mr-2 h-4 w-4" />
                        Esborrar
                      </Button>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  )
}

