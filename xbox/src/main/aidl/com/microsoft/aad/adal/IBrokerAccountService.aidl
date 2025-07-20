package com.microsoft.aad.adal;

interface IBrokerAccountService {

    Bundle getBrokerUsers();

    Bundle acquireTokenSilently(in Map requestParameters);

    Intent getIntentForInteractiveRequest();

    void removeAccounts();
}