using System;
using System.Threading;
using System.Diagnostics;
using System.Management;

public class TestPerfCounter
{
    static PerformanceCounter myCounter;

    public static void Main()
    {      
        myCounter = new PerformanceCounter("Processor", @"% Processor Time", @"_Total");
        try
        {
            myCounter.NextValue().ToString();
            Thread.Sleep(100);
            Console.WriteLine("cpuPercentUsage=" + myCounter.NextValue().ToString());
        }

        catch
        {
            return;
        }
        try
        {
            ManagementObjectSearcher searcher = new ManagementObjectSearcher(@"root\WMI", "SELECT * FROM MSAcpi_ThermalZoneTemperature");
            int i = 0;
            foreach (ManagementObject obj in searcher.Get())
            {
                Double temp = Convert.ToDouble(obj["CurrentTemperature"].ToString());
                temp = (temp - 2732) / 10.0;
                Console.WriteLine("cpuTemp" + i++ + "=" + temp);
            }
        }
        catch
        {
            return;
        }


    }
}
