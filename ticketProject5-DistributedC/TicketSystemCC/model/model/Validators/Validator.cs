using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace model.Validators
{
    public interface Validator<E>
    {
        void Validate(E entity);
    }
}
