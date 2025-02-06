'use client'

import { useRouter } from 'next/navigation'
import { useState } from 'react'
import { Avatar, AvatarFallback, AvatarImage} from '@/components/ui/avatar'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Label } from '@/components/ui/label'
import { Briefcase, Calendar, Mail, Phone, User } from 'lucide-react'
import { useToast } from '@/hooks/use-toast'

type Rols = 'ADMINISTRADOR' | 'GESTOR_PROJECTE' | 'TREBALLADOR'

type User = {
  username: string
  nom: string
  edat: number
  tlf: number
  email: string
  rol: Rols
}

export function EditUserForm({ initialUser }: { initialUser: User }) {
  const router = useRouter()
  const [user, setUser] = useState<User>(initialUser)
  const { toast } = useToast()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    try {
      const response = await fetch(`http://10.4.41.33:8080/usuaris/${user.username}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
      })

      if (!response.ok) {
        throw new Error('Failed to update user')
      }

      toast({
        title: "Success",
        description: "User updated successfully",
      })

      router.push('/dashboard/users')
    } catch (error) {
      console.error('Error updating user:', error)
      toast({
        title: "Error",
        description: "Failed to update user. Please try again.",
        variant: "destructive",
      })
    }
  }

  return (
    <Card className="w-full max-w-2xl mx-auto">
      <CardHeader className="space-y-1">
        <div className="flex items-center space-x-4">
          <Avatar className="w-20 h-20">
            <AvatarImage src={`https://api.dicebear.com/6.x/initials/svg?seed=${user.nom}`} />
            <AvatarFallback>{user.nom.split(' ').map(n => n[0]).join('')}</AvatarFallback>
          </Avatar>
          <div>
            <CardTitle className="text-2xl font-bold">{user.nom}</CardTitle>
            <p className="text-sm text-muted-foreground">@{user.username}</p>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-8">
          <div className="space-y-4">
            <h3 className="text-lg font-semibold">Informació Personal</h3>
            <div className="grid gap-4 sm:grid-cols-2">
              <div className="space-y-2">
                <Label htmlFor="nom" className="flex items-center space-x-2">
                  <User className="w-4 h-4" />
                  <span>Nom</span>
                </Label>
                <Input
                  id="nom"
                  value={user.nom}
                  onChange={(e) => setUser({ ...user, nom: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="email" className="flex items-center space-x-2">
                  <Mail className="w-4 h-4" />
                  <span>Email</span>
                </Label>
                <Input
                  id="email"
                  type="email"
                  value={user.email}
                  onChange={(e) => setUser({ ...user, email: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="edat" className="flex items-center space-x-2">
                  <Calendar className="w-4 h-4" />
                  <span>Edat</span>
                </Label>
                <Input
                  id="edat"
                  type="number"
                  value={user.edat}
                  onChange={(e) => setUser({ ...user, edat: parseInt(e.target.value) })}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="tlf" className="flex items-center space-x-2">
                  <Phone className="w-4 h-4" />
                  <span>Telèfon</span>
                </Label>
                <Input
                  id="tlf"
                  type="tel"
                  value={user.tlf}
                  onChange={(e) => setUser({ ...user, tlf: parseInt(e.target.value) })}
                />
              </div>
            </div>
          </div>
          <div className="space-y-4">
            <div className="grid gap-4 sm:grid-cols-2">
              <div className="space-y-2">
                <Label htmlFor="rol" className="flex items-center space-x-2">
                  <Briefcase className="w-4 h-4" />
                  <span>Rol</span>
                </Label>
                <Select
                  value={user.rol}
                  onValueChange={(value: Rols) => setUser({ ...user, rol: value })}
                >
                  <SelectTrigger id="rol">
                    <SelectValue placeholder="Selecciona un rol" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="ADMINISTRADOR">Administrador</SelectItem>
                    <SelectItem value="GESTOR_PROJECTE">Gestor de Projecte</SelectItem>
                    <SelectItem value="TREBALLADOR">Treballador</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>
          </div>
          
          <div className="flex justify-end space-x-4">
            <Button type="button" variant="outline" onClick={() => router.push('/dashboard/users')}>
              Cancel·lar
            </Button>
            <Button type="submit">Desar Canvis</Button>
          </div>
        </form>
      </CardContent>
    </Card>
  )
}

