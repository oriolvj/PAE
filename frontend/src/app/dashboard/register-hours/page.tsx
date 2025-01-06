'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { useToast } from "@/hooks/use-toast"

type ProjectEntry = {
  projectName: string
  totalHours: number
  tecnic_hours: { [key: string]: number }[]
}

export default function RegisterHoursPage() {
  const [entries, setEntries] = useState<ProjectEntry[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const { toast } = useToast()

  useEffect(() => {
    fetchEntries()
  }, [])

  const fetchEntries = async () => {
    setIsLoading(true)
    try {
      const response = await fetch('http://10.4.41.40:8080/registrehores')
      if (!response.ok) {
        throw new Error('Failed to fetch entries')
      }
      const data = await response.json()
      setEntries(data)
    } catch (error) {
      console.error('Error fetching entries:', error)
      toast({
        title: "Error",
        description: "No s'han pogut carregar les entrades. Torna-ho a provar.",
        variant: "destructive",
      })
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="container mx-auto py-10">
      <Card>
        <CardHeader>
          <CardTitle>Registre d'hores treballades</CardTitle>
        </CardHeader>
        <CardContent>
          {isLoading ? (
            <div className="text-center mt-6">Carregant...</div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Projecte</TableHead>
                  <TableHead>Empleat</TableHead>
                  <TableHead>Hores</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {entries.map((entry) => (
                  <>
                    {entry.tecnic_hours.map((tecnic, index) => {
                      const [username, hours] = Object.entries(tecnic)[0];
                      return (
                        <TableRow key={`${entry.projectName}-${username}`}>
                          {index === 0 && (
                            <TableCell rowSpan={entry.tecnic_hours.length + 1}>
                              {entry.projectName}
                            </TableCell>
                          )}
                          <TableCell>{username}</TableCell>
                          <TableCell>{hours}</TableCell>
                        </TableRow>
                      );
                    })}
                    <TableRow key={`${entry.projectName}-total`} className="font-bold">
                      <TableCell>Total</TableCell>
                      <TableCell>{entry.totalHours}</TableCell>
                    </TableRow>
                  </>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

