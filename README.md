# CentDB_G16

A centralised database management system for relational databases.

Modules and APIs
* CommandLineInterface
* UserAuthentication
    - Authentication
    - Validator
    - TokenManager
* QueryProcessing
    - QueryParser
    - QueryValidator
    - Executor
        - SingleQueryExecutor
        - TransactionQueryExecutor
* LogManagement
* DataModelling
    - Modeller
    - Mapper
* DataDump
    - ExportStructureValue
    - ImportStructureValue
* Analytics
* DataStoringAndParsing
    - FileParsing
    - FileReaderWriter