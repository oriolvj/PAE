'use client'

import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

type HourEntry = {
  id: string
  employee: string
  project: string
  date: string
  hours: number
}

export default function RegisterHoursPage() {
  const [entries, setEntries] = useState<HourEntry[]>([
    { id: '1', employee: 'Joan Pérez', project: 'Projecte A', date: '2023-05-15', hours: 8 },
    { id: '2', employee: 'Maria García', project: 'Projecte B', date: '2023-05-15', hours: 7.5 },
  ])

  const [newEntry, setNewEntry] = useState<Omit<HourEntry, 'id'>>({
    employee: '',
    project: '',
    date: '',
    hours: 0
  })

  const addEntry = () => {
    if (newEntry.employee && newEntry.project && newEntry.date && newEntry.hours > 0) {
      const id = (entries.length + 1).toString()
      setEntries([...entries, { ...newEntry, id }])
      setNewEntry({ employee: '', project: '', date: '', hours: 0 })
    }
  }

  return (
    <div className="container mx-auto py-10">
      <Card>
        <CardHeader>
          <CardTitle>Registre d'hores treballades</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <Select
                value={newEntry.employee}
                onValueChange={(value) => setNewEntry({ ...newEntry, employee: value })}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Seleccionar empleat" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="Joan Pérez">Joan Pérez</SelectItem>
                  <SelectItem value="Maria García">Maria García</SelectItem>
                </SelectContent>
              </Select>
              <Select
                value={newEntry.project}
                onValueChange={(value) => setNewEntry({ ...newEntry, project: value })}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Seleccionar projecte" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="Projecte A">Projecte A</SelectItem>
                  <SelectItem value="Projecte B">Projecte B</SelectItem>
                </SelectContent>
              </Select>
              <Input
                type="date"
                value={newEntry.date}
                onChange={(e) => setNewEntry({ ...newEntry, date: e.target.value })}
              />
              <Input
                type="number"
                placeholder="Hores treballades"
                value={newEntry.hours || ''}
                onChange={(e) => setNewEntry({ ...newEntry, hours: parseFloat(e.target.value) })}
              />
            </div>
            <Button onClick={addEntry}>Afegir registre</Button>
          </div>
          <Table className="mt-6">
            <TableHeader>
              <TableRow>
                <TableHead>Empleat</TableHead>
                <TableHead>Projecte</TableHead>
                <TableHead>Data</TableHead>
                <TableHead>Hores</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {entries.map((entry) => (
                <TableRow key={entry.id}>
                  <TableCell>{entry.employee}</TableCell>
                  <TableCell>{entry.project}</TableCell>
                  <TableCell>{entry.date}</TableCell>
                  <TableCell>{entry.hours}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  )
}

