import { redirect } from 'next/navigation'
import { cookies } from 'next/headers'

/*async function getUser() {
  const cookieStore = cookies()
  const token = cookieStore.get('token')
  
  if (!token) {
    return null
  }

  // Here you would typically validate the token and fetch user data
  // This is a simplified example
  return { id: '1', name: 'John Doe' }
}*/

export default async function Home() {
  //const user = await getUser()
  redirect('/dashboard')
  /*if (user) {
    redirect('/dashboard')
  } else {
    redirect('/login')
  }*/

  // This part will never be reached due to the redirects above,
  // but we need to return something to satisfy TypeScript
  return null
}

