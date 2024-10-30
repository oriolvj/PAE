'use client'

import React, { useState } from 'react'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent } from "@/components/ui/card"
import { Checkbox } from "@/components/ui/checkbox"
export default function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log('Login attempted with:', { email, password })

    if(email == "admin@admin.com" && password == "admin"){
      window.location.href = '/dashboard';
    } else {
      alert('Invalid Credentials');
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
                <Label htmlFor="email" className="text-sm font-medium text-gray-700">Correu</Label>
                <Input 
                  id="email" 
                  type="email" 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
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
                  <Label htmlFor="remember" className="text-sm text-gray-600">Recorda'm</Label>
                </div>
                <a href="#" className="text-sm text-gray-600 hover:underline">Contrasenya oblidada?</a>
              </div>
              <Button type="submit" className="w-full bg-red-600 hover:bg-red-700 text-white py-3 px-4 rounded-xl transition duration-300">
                Accedir
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}