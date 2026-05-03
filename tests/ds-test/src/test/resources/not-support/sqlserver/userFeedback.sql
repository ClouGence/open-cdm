CREATE TABLE [CARD_ORDER](
    [Id] [int] IDENTITY(1,1) NOT FOR REPLICATION NOT NULL,
    [OrderId] [varchar](32) NULL,
    [Number] [varchar](24) NULL,
    [Num] [int] NULL,
    [Price] [decimal](18, 2) NULL,
    [TotalAmount] [decimal](18, 2) NULL,
    [Status] [varchar](2) NULL,
    [CreateTime] [varchar](20) NULL,
    [TxnTime] [varchar](20) NULL,
    [IsOpen] [char](1) NULL
    ) ON [PRIMARY];
--------------------------