Feature: Sample

  @Test
  Scenario Outline: sample test
    Given login to the application with data (<URL>,<userid>,<password>)...Login:openURLAndEnterloginDetailsAndSubmit
    And get balance value for the acct with data (<AcctNbr>)...HomePage:openAcctSummaryAndGetBal

    @Test
    Examples: 
      | URL                                | userid   | password    | AcctNbr         |
      | https://www.unionbankonline.co.in/ | 58051530 | Akveer@nki4 | 035610100156184 |
