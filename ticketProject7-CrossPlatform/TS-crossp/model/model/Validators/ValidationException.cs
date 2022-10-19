using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace model.Validators
{

    /**
     * Helps catching exceptions
     * Throws ValidateException if exception encountered.
     */
    public class ValidationException : Exception
    {
        public ValidationException() { }

        public ValidationException(String message)
        {
            Console.WriteLine(message);
        }

    }

}
