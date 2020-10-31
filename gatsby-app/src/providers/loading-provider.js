import React, { createContext, useEffect, useState } from 'react'

export const LoadingContext = createContext({
  isLoading: false,
  value: 0,
})

const LoadingProvider = ({ children }) => {
  const [loading, setLoading] = useState({
    isLoading: false,
    value: 0,
  })

  useEffect(() => {
    if (loading.isLoading) {
      window.document.title = `UP | ${loading.value}%`
    } else {
      window.document.title = `UP | Share your dreams`
    }
  }, [loading])

  const values = {
    loading,
    setLoading,
  }

  return (
    <LoadingContext.Provider value={values}>
      {children}
    </LoadingContext.Provider>
  )
}

export default LoadingProvider
