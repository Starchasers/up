import React, { createContext, useState } from 'react'

export const PAGE = {
  MAIN_PAGE: 0,
  LOADING_PAGE: 1,
  ERROR_PAGE: 2,
  AFTER_UPLOAD_PAGE: 3,
}


export const PageContext = createContext({
  page: PAGE.MAIN_PAGE
})

const PageProvider = ({ children }) => {
  const [page, setPage] = useState(PAGE.MAIN_PAGE)
  const [response, setResponse] = useState({ data: {}})
  const [error, setError] = useState({ message: '', status: 200 })

  const value = {
    page,
    setPage,
    response,
    setResponse,
    error,
    setError
  }

  return (
    <PageContext.Provider value={value}>
      {children}
    </PageContext.Provider>
  )
}

export default PageProvider
