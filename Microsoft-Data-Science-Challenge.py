for i in range(0, num_inputs):
        end_str = dataframe1['RISK_LAPSE'][i]
        if (isinstance(end_str, str)):
            if (end_str != "#VALUE!"):
                end_date = to_date(end_str)
            elif (end_str == "#VALUE!"):
                end_date = to_date(dataframe1['DOB_ANON'][i]) + relativedelta(years=100)
        else:
            end_date = None
        dif = None if (end_date == None) else (end_date - to_date(dataframe1['COMMENCEMENT_DATE_ANON'][i]))
        duration.append(-1 if (dif==None) else dif.days)
        dataframe1['annualincome'][i] = max(dataframe1['annualincome'][i], dataframe1['currentannualincome'][i])
        
        death_str = dataframe1['Date Incident'][i]
        death_date = to_date(death_str)
        diff_days = death_date - to_date(dataframe1['COMMENCEMENT_DATE_ANON (2)'][i])
        daystodeath.append(diff_days.days)

        diff_yrs = relativedelta(death_date, to_date(dataframe1['DOB_ANON (2)'][i])).years
        ageofdeath.append(diff_yrs)
