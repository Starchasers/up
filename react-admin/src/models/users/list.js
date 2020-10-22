import React from 'react'
import {
  List,
  Datagrid,
  TextField,
  EditButton,
  Pagination
} from 'react-admin'

const UserPagination = (props) => (
  <Pagination rowsPerPageOptions={[10, 15, 25, 50, 100]} {...props} />
)

export default (props) => (
  <List
    {...props}
    bulkActionButtons={false}
    perPage={15}
    pagination={<UserPagination/>}
  >
    <Datagrid>
      <TextField
        source='id'
      />
      <TextField
        source='username'
      />
      <TextField
        source='email'
      />
      <TextField
        source='role'
      />
      <EditButton />
    </Datagrid>
  </List>
)
