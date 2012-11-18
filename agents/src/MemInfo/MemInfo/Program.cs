using System;
using System.Management;

namespace MemInfo
{
    class Program
    {
        static void Main(string[] args)
        {
            ObjectQuery winQuery = new ObjectQuery("SELECT * FROM CIM_OperatingSystem");

            ManagementObjectSearcher searcher = new ManagementObjectSearcher(winQuery);

            foreach (ManagementObject item in searcher.Get())
            {
                Console.WriteLine("TotalVirtualMemorySize=" + item["TotalVirtualMemorySize"]);
                Console.WriteLine("TotalVisibleMemorySize=" + item["TotalVisibleMemorySize"]);
                Console.WriteLine("NumberOfProcesses=" + item["NumberOfProcesses"]);
                Console.WriteLine("FreeVirtualMemory=" + item["FreeVirtualMemory"]);
                Console.WriteLine("FreeSpaceInPagingFiles=" + item["FreeSpaceInPagingFiles"]);
                Console.WriteLine("FreePhysicalMemory=" + item["FreePhysicalMemory"]);
            }
        }
    }
}