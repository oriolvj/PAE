'use client'

import { Card, CardContent } from "@/components/ui/card"

const variables = [
  'Complir conveni d\'hores treballades',
  'Prioritzar als treballadors de l\'empresa per davant dels externs',
  'Respectar els descansos mínims entre jornades',
  'Distribuir equitativament les hores extra entre els treballadors',
  'Mantenir un equilibri entre projectes interns i externs'
]

export default function VariablesPage() {
  return (
    <div className="container mx-auto py-10">
      <Card className="bg-zinc-950 text-white">
        <CardContent className="p-6">
          <ul className="space-y-4">
            {variables.map((variable, index) => (
              <li key={index} className="flex items-start gap-2">
                <span className="mt-1">•</span>
                <span>{variable}</span>
              </li>
            ))}
          </ul>
        </CardContent>
      </Card>
    </div>
  )
}

