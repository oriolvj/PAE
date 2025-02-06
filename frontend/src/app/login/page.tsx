'use client'

import React, { useState } from 'react'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent } from "@/components/ui/card"
import { Checkbox } from "@/components/ui/checkbox"
import { useRouter } from 'next/navigation'

export default function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')
  const router = useRouter()


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)
    setError('')

    try {
      const response = await fetch('http://10.4.41.33:8080/usuaris/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify({ username, password }),
        credentials: 'omit',
        mode: 'cors',
      })

      if (!response.ok) {
        throw new Error('Invalid credentials')
      }

      const data = await response.json()
      console.log('Login successful:', data)
      
      // Store the token in a cookie
      //setCookie('token', data.token, { maxAge: 60 * 60 * 24 * 7 }) // 1 week

      router.push('/dashboard')
    } catch (err) {
      console.error('Login error:', err)
      setError('Invalid credentials or server error. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center bg-gradient-to-r from-black to-white">
      <div className="w-full max-w-md ml-[10%]">
        <Card className="bg-white rounded-3xl shadow-lg overflow-hidden">
          <CardContent className="p-8">
            <h1 className="text-3xl font-bold text-red-600 mb-8 text-center">Accedeix al teu compte</h1>
            <form onSubmit={handleSubmit} className="space-y-6">
              <div className="space-y-2">
                <Label htmlFor="username" className="text-sm font-medium text-gray-700">Nom d&apos;usuari</Label>
                <Input 
                  id="username" 
                  type="text" 
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                  className="w-full p-3 border border-gray-300 rounded-xl"
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="password" className="text-sm font-medium text-gray-700">Contrasenya</Label>
                <Input 
                  id="password" 
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  className="w-full p-3 border border-gray-300 rounded-xl"
                />
              </div>
              <div className="flex items-center justify-between">
                <div className="flex items-center">
                  <Checkbox id="remember" className="mr-2 rounded" />
                  <Label htmlFor="remember" className="text-sm text-gray-600">Recorda&apos;m</Label>
                </div>
                <a href="#" className="text-sm text-gray-600 hover:underline">Contrasenya oblidada?</a>
              </div>
              {error && <p className="text-red-500 text-sm" role="alert">{error}</p>}
              <Button 
                type="submit" 
                className="w-full bg-red-600 hover:bg-red-700 text-white py-3 px-4 rounded-xl transition duration-300"
                disabled={isLoading}
              >
                {isLoading ? 'Carregant...' : 'Accedir'}
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

