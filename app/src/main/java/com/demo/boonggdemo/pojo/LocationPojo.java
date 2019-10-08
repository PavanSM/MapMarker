package com.demo.boonggdemo.pojo;

public class LocationPojo
    {
        private String pancardNumer;

        private String address;


        private String latitude;



        private String isActive;

        private String gstNumber;

        private String __v;

        private String[] contact;

        private String name;

        private String _id;

        private String startDate;

        private String longitude;

        public String getPancardNumer ()
        {
            return pancardNumer;
        }

        public void setPancardNumer (String pancardNumer)
        {
            this.pancardNumer = pancardNumer;
        }

        public String getAddress ()
        {
            return address;
        }

        public void setAddress (String address)
        {
            this.address = address;
        }



        public String getLatitude ()
        {
            return latitude;
        }

        public void setLatitude (String latitude)
        {
            this.latitude = latitude;
        }



        public String getIsActive ()
        {
            return isActive;
        }

        public void setIsActive (String isActive)
        {
            this.isActive = isActive;
        }

        public String getGstNumber ()
        {
            return gstNumber;
        }

        public void setGstNumber (String gstNumber)
        {
            this.gstNumber = gstNumber;
        }

        public String get__v ()
        {
            return __v;
        }

        public void set__v (String __v)
        {
            this.__v = __v;
        }

        public String[] getContact ()
        {
            return contact;
        }

        public void setContact (String[] contact)
        {
            this.contact = contact;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String get_id ()
        {
            return _id;
        }

        public void set_id (String _id)
        {
            this._id = _id;
        }

        public String getStartDate ()
        {
            return startDate;
        }

        public void setStartDate (String startDate)
        {
            this.startDate = startDate;
        }

        public String getLongitude ()
        {
            return longitude;
        }

        public void setLongitude (String longitude)
        {
            this.longitude = longitude;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [pancardNumer = "+pancardNumer+", address = "+address+ ", latitude = "+latitude+ ", isActive = "+isActive+", gstNumber = "+gstNumber+", __v = "+__v+", contact = "+contact+", name = "+name+", _id = "+_id+", startDate = "+startDate+", longitude = "+longitude+"]";
        }
    }
