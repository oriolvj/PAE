'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Switch } from "@/components/ui/switch"
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from "@/components/ui/card"
import { ArrowLeft } from 'lucide-react'
import { useToast } from "@/hooks/use-toast"
import { Toaster } from "@/components/ui/toaster"

type Rols = 'ADMINISTRADOR' | 'GESTOR_PROJECTE' | 'TREBALLADOR'
type Jornada = "TOTAL" | "PARCIAL" | "TRENTA_HORES" | "ALTRES"

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

export default function AddUserPage() {
  const router = useRouter()
  const { toast } = useToast()
  const [isLoading, setIsLoading] = useState(false)
  const [user, setUser] = useState<User>({
    username: '',
    nom: '',
    edat: 0,
    tlf: 0,
    email: '',
    pwd: '',
    rol: 'TREBALLADOR',
    preferencia: '',
    actiu: true,
    contractat: false,
    jornada: 'TOTAL'
  })

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type } = e.target
    setUser(prev => ({
      ...prev,
      [name]: type === 'number' ? Number(value) : value
    }))
  }

  const handleSelectChange = (name: string) => (value: string) => {
    setUser(prev => ({ ...prev, [name]: value }))
  }

  const handleSwitchChange = (name: string) => (checked: boolean) => {
    setUser(prev => ({ ...prev, [name]: checked }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)

    try {
      const response = await fetch('http://10.4.41.40:8080/usuaris', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
      })
      
      if (!response.ok) {
        console.error('Response:', response)
        throw new Error('Failed to create user')
      }

      const data = await response.json()
      toast({
        title: "Success",
        description: "User created successfully",
      })
      router.push('/dashboard/users')
    } catch (error) {
      
      console.error('Error creating user:', error)
      toast({
        title: "Error",
        description: "Failed to create user. Please try again.",
        variant: "destructive",
      })
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="container mx-auto py-10">
      <Button variant="ghost" onClick={() => router.push('/dashboard/users')} className="mb-6">
        <ArrowLeft className="mr-2 h-4 w-4" /> Anar a la llista d'usuaris
      </Button>
      <Card className="max-w-2xl mx-auto">
        <CardHeader>
          <CardTitle>Crear un nou usuari</CardTitle>
          <CardDescription>Introdueix els detalls del nou usuari.</CardDescription>
        </CardHeader>
        <form onSubmit={handleSubmit}>
          <CardContent className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="username">Usuari</Label>
                <Input id="username" name="username" value={user.username} onChange={handleInputChange} required />
              </div>
              <div className="space-y-2">
                <Label htmlFor="nom">Nom</Label>
                <Input id="nom" name="nom" value={user.nom} onChange={handleInputChange} required />
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="edat">Edat</Label>
                <Input id="edat" name="edat" type="number" value={user.edat} onChange={handleInputChange} required />
              </div>
              <div className="space-y-2">
                <Label htmlFor="tlf">Telèfon</Label>
                <Input id="tlf" name="tlf" type="number" value={user.tlf} onChange={handleInputChange} required />
              </div>
            </div>
            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input id="email" name="email" type="email" value={user.email} onChange={handleInputChange} required />
            </div>
            <div className="space-y-2">
              <Label htmlFor="pwd">Contrasenya</Label>
              <Input id="pwd" name="pwd" type="password" value={user.pwd} onChange={handleInputChange} required />
            </div>
            <div className="space-y-2">
              <Label htmlFor="rol">Rol</Label>
              <Select name="rol" value={user.rol} onValueChange={handleSelectChange('rol')}>
                <SelectTrigger>
                  <SelectValue placeholder="Select a role" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ADMINISTRADOR">Administrador</SelectItem>
                  <SelectItem value="TREBALLADOR">Treballador</SelectItem>
                  <SelectItem value="GESTOR_PROJECTE">Gestor del Projecte</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label htmlFor="preferencia">Preferència</Label>
              <Input id="preferencia" name="preferencia" value={user.preferencia} onChange={handleInputChange} />
            </div>
            <div className="space-y-2">
              <Label htmlFor="jornada">Jornada del Treballador</Label>
              <Select name="jornada" value={user.jornada} onValueChange={handleSelectChange('jornada')}>
                <SelectTrigger>
                  <SelectValue placeholder="Selecciona la jornada del treballador" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="TOTAL">Completa</SelectItem>
                  <SelectItem value="PARCIAL">Parcial</SelectItem>
                  <SelectItem value="TRENTA_HORES">30 hores</SelectItem>
                  <SelectItem value="ALTRES">Altres</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="flex items-center space-x-2">
              <Switch id="actiu" checked={user.actiu} onCheckedChange={handleSwitchChange('actiu')} />
              <Label htmlFor="actiu">Actiu</Label>
            </div>
            <div className="flex items-center space-x-2">
              <Switch id="contractat" checked={user.contractat} onCheckedChange={handleSwitchChange('contractat')} />
              <Label htmlFor="contractat">Contractat</Label>
            </div>
          </CardContent>
          <CardFooter>
            <Button type="submit" className="w-full" disabled={isLoading}>
              {isLoading ? 'Afegint usuari...' : 'Usuari afegit'}
            </Button>
          </CardFooter>
        </form>
      </Card>
      <Toaster />
    </div>
  )
}

